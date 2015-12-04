package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.path.Tokenizer;
import com.kumiq.identity.scim.utils.ValueUtils;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Compiler for SCIM filter. Note this object is stateful.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings(value = "unchecked")
public class FilterCompiler {

    private Stack<FilterToken> outputStack;
    private Stack<FilterToken> operatorStack;

    public static Predicate compile(String filter) {
        FilterCompiler compiler = new FilterCompiler();
        return compiler.doCompile(filter);
    }

    private FilterCompiler() {
        this.outputStack = new Stack<>();
        this.operatorStack = new Stack<>();
    }

    private Predicate doCompile(String filter) {
        filter = ValueUtils.stripSquareBrackets(filter);
        Tokenizer filterTokenizer = new Tokenizer.FilterTokenizer(filter);
        List<FilterToken> tokens = new ArrayList<>();

        while (true) {
            try {
                String token = filterTokenizer.nextSequence().toString();
                tokens.add(FilterTokenFactory.create(token));
            } catch (Tokenizer.NoMoreSequenceException ex) {
                break;
            }
        }

        tokens.forEach(filterToken -> {
            if (filterToken.isOperand()) {
                pushOntoOutputStack(filterToken);
            } else {
                try {
                    pushOntoOperatorStack(filterToken);
                } catch (InsufficientPriorityException ipe) {
                    Queue<FilterToken> popped = popFromOperatorStackWithPrecedenceNoLessThan(((OperatorAware) filterToken).precedence());
                    while (popped.size() > 0)
                        pushOntoOutputStack(popped.poll());

                    try {
                        pushOntoOperatorStack(filterToken);
                    } catch (Exception ex) {
                        fail();
                    }
                } catch (RightParenthesisPushedException rpe) {
                    Queue<FilterToken> popped = popFromOperatorStackToLeftParenthesis();
                    while (popped.size() > 0)
                        pushOntoOutputStack(popped.poll());
                    Assert.isTrue(this.operatorStack.pop().equals(Bracket.LEFT));
                }
            }
        });

        while (this.operatorStack.size() > 0) {
            FilterToken popped = this.operatorStack.pop();
            if (!popped.isOperator())
                fail();
            pushOntoOutputStack(popped);
        }

        Assert.isTrue(this.outputStack.size() == 1);
        Assert.isTrue(this.outputStack.peek().isOperator());
        return (Predicate) this.outputStack.pop();
    }

    private Queue<FilterToken> popFromOperatorStackToLeftParenthesis() {
        Queue<FilterToken> queue = new LinkedList<>();
        for (FilterToken first = this.operatorStack.peek(); this.operatorStack.size() > 0; first = this.operatorStack.peek()) {
            if (first.equals(Bracket.LEFT))
                break;
            queue.add(this.operatorStack.pop());
        }
        if (this.operatorStack.size() == 0 || !this.operatorStack.peek().equals(Bracket.LEFT))
            fail();
        return queue;
    }

    private Queue<FilterToken> popFromOperatorStackWithPrecedenceNoLessThan(int precedence) {
        Queue<FilterToken> queue = new LinkedList<>();
        try {
            for (FilterToken first = this.operatorStack.peek(); this.operatorStack.size() > 0; first = this.operatorStack.peek()) {
                if (first.isOperator()) {
                    if (((OperatorAware) first).precedence() >= precedence)
                        queue.add(this.operatorStack.pop());
                    else
                        break;
                } else {
                    break;
                }
            }
        } catch (EmptyStackException ex) {
            fail();
        }
        return queue;
    }

    private void pushOntoOperatorStack(FilterToken filterToken) throws InsufficientPriorityException, RightParenthesisPushedException{
        if (filterToken.equals(Bracket.RIGHT))
            throw new RightParenthesisPushedException();

        if (filterToken.isOperator()) {
            if (this.operatorStack.size() > 0) {
                FilterToken peek = this.operatorStack.peek();
                if (peek.isOperator()) {
                    OperatorAware operatorToPush = (OperatorAware) filterToken;
                    OperatorAware firstInStack = (OperatorAware) peek;

                    if (operatorToPush.associtivity().equals(OperatorAware.Associtivity.LEFT) &&
                            operatorToPush.precedence() <= firstInStack.precedence()) {
                        throw new InsufficientPriorityException();
                    } else if (operatorToPush.associtivity().equals(OperatorAware.Associtivity.RIGHT) &&
                            operatorToPush.precedence() < firstInStack.precedence()) {
                        throw new InsufficientPriorityException();
                    }
                }
            }
        }

        this.operatorStack.push(filterToken);
    }

    private void pushOntoOutputStack(FilterToken filterToken) {
        if (filterToken.isOperator()) {
            FilterToken firstToken = this.outputStack.pop();
            if (firstToken == null)
                fail();

            if (isPrNode(filterToken) || isNotNode(filterToken)) {
                ((ExpressionNode) filterToken).setLeft(firstToken);
                ((ExpressionNode) filterToken).setRight(ValueNodeFactory.nullNode());
            } else {
                ((ExpressionNode) filterToken).setRight(firstToken);
                FilterToken secondToken = this.outputStack.pop();
                if (secondToken == null)
                    fail();
                ((ExpressionNode) filterToken).setLeft(secondToken);
            }
        } else if (filterToken.isParenthesis()) {
            fail();
        }

        this.outputStack.push(filterToken);
    }

    private boolean isPrNode(FilterToken token) {
        return (token instanceof ExpressionNode.RelationalExpressionNode) &&
                ((ExpressionNode.RelationalExpressionNode) token).getOperator().equals(RelationalOperator.PR);
    }

    private boolean isNotNode(FilterToken token) {
        return (token instanceof ExpressionNode.LogicalExpressionNode) &&
                ((ExpressionNode.LogicalExpressionNode) token).getOperator().equals(LogicalOperator.NOT);
    }

    private void fail() {
        throw new RuntimeException("invalid token sequence");
    }

    private static class InsufficientPriorityException extends Exception {}
    private static class RightParenthesisPushedException extends Exception {}
}
