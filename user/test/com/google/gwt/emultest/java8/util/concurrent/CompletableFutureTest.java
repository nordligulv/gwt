/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.emultest.java8.util.concurrent;

import com.google.gwt.emultest.java.util.EmulTestBase;
import com.google.gwt.user.client.Timer;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * Tests for java.util.concurrent.CompletableFutureTest.
 */
public class CompletableFutureTest extends EmulTestBase {

  public void testRunAsync_ShouldCompleteWithNullValue_WhenComplete() {
    assertThat(runAsync())
        .willComplete()
        .withResult(null);
  }

  public void testRunAsync_ShouldCompleteWithCancellationException_WhenCancelled() {
    CompletableFuture<Void> future = runAsync();

    assertThat(future)
        .willComplete()
        .withException(new CancellationException());

    future.cancel(true);
  }

  public void testRunAsync_ShouldCompleteWithCompletionException_WhenThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<Void> future = runAsyncWithException(taskException);

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testRunAsync_ShouldCompleteWithValue_WhenCalledObtrudeValue() {
    CompletableFuture<Void> future = runAsync();

    assertThat(future)
        .willComplete()
        .withResult(null);

    future.obtrudeValue(null);
  }

  public void testRunAsync_ShouldCompleteWithException_WhenCalledObtrudeException() {
    RuntimeException taskException1 = new RuntimeException();
    RuntimeException taskException2 = new RuntimeException();
    CompletableFuture<Void> future = runAsyncWithException(taskException1);

    assertThat(future)
        .willComplete()
        .withException(taskException2);

    future.obtrudeException(taskException2);
  }

  public void testRunAsync_ShouldCompleteWithValue2_WhenCalledCompleteValue2() {
    CompletableFuture<Void> future = runAsyncWithException(new RuntimeException());

    assertThat(future)
        .willComplete()
        .withResult(null);

    future.complete(null);
  }

  public void testRunAsync_ShouldCompleteWithException_WhenCalledCompleteExceptionally() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<Void> future = runAsync();

    assertThat(future)
        .willComplete()
        .withException(taskException);

    future.completeExceptionally(taskException);
  }

  public void testRunAsync_ShouldComplete_WhenCalledExceptionally() {
    CompletableFuture<Void> future = runAsyncWithException(new RuntimeException())
        .exceptionally(e -> null);

    assertThat(future)
        .willComplete()
        .withResult(null);
  }

  public void testSupplyAsync_ShouldCompleteWithValue_WhenComplete() {
    Object result = new Object();
    CompletableFuture<Object> future = supplyAsyncWithResult(result);

    assertThat(future)
        .willComplete()
        .withResult(result);
  }

  public void testSupplyAsync_ShouldCompleteWithCancellationException_WhenCancelled() {
    CompletableFuture<Void> future = supplyAsyncWithResult(null);

    assertThat(future)
        .willComplete()
        .withException(new CancellationException());

    future.cancel(true);
  }

  public void testSupplyAsync_ShouldCompleteWithCompletionException_WhenThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<Object> future = supplyAsyncWithException(taskException);

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_ShouldCompleteWithValue_WhenCalledObtrudeValue() {
    Object result = new Object();
    CompletableFuture<Object> future = supplyAsyncWithResult(null);

    assertThat(future)
        .willComplete()
        .withResult(result);

    future.obtrudeValue(result);
  }

  public void testSupplyAsync_ShouldCompleteWithException_WhenCalledObtrudeException() {
    RuntimeException taskException1 = new RuntimeException();
    RuntimeException taskException2 = new RuntimeException();
    CompletableFuture<Object> future = supplyAsyncWithException(taskException1);

    assertThat(future)
        .willComplete()
        .withException(taskException2);

    future.obtrudeException(taskException2);
  }

  public void testSupplyAsync_ShouldCompleteWithValue2_WhenCalledCompleteValue2() {
    Object value1 = new Object();
    Object value2 = new Object();
    CompletableFuture<Object> future = supplyAsyncWithResult(value1);

    assertThat(future)
        .willComplete()
        .withResult(value2);

    future.complete(value2);
  }

  public void testSupplyAsync_ShouldCompleteWithException_WhenCalledCompleteExceptionally() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<Object> future = supplyAsyncWithResult(new Object());

    assertThat(future)
        .willComplete()
        .withException(taskException);

    future.completeExceptionally(taskException);
  }

  public void testSupplyAsync_ShouldComplete_WhenCalledExceptionally() {
    String result = "result";
    RuntimeException runException = new RuntimeException(result);
    CompletableFuture<Object> future = supplyAsyncWithException(runException)
        .exceptionally(e -> e.getCause().getMessage());

    assertThat(future)
        .willComplete()
        .withResult(result);
  }

  public void testSupplyAsync_thenApply() {
    CompletableFuture<String> future = supplyAsyncWithResult("a")
        .thenApply(v -> v + "b");

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withResult("ab");
  }

  public void testSupplyAsync_thenApplyShouldFail_WhenThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<String> future = supplyAsyncWithResult("a")
        .thenApply(v -> {
          throw taskException;
        });

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_thenApplyShouldFail_WhenFutureThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<String> future = supplyAsyncWithException(taskException)
        .thenApply(v -> {
          fail(getTag() + "should not be called");
          return null;
        });

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_thenAccept() {
    CompletableFuture<Void> future = supplyAsyncWithResult("a")
        .thenAccept(v -> assertEquals("a", v));

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withResult(null);
  }

  public void testSupplyAsync_thenAcceptShouldFail_WhenThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<Void> future = supplyAsyncWithResult("a")
        .thenAccept(v -> {
          throw taskException;
        });

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_thenAcceptShouldFail_WhenFutureThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<Void> future = supplyAsyncWithException(taskException)
        .thenAccept(v -> fail(getTag() + "should not be called"));

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_thenRun() {
    CompletableFuture<Void> future = supplyAsyncWithResult("a")
        .thenRun(() -> {}); // TODO: test that it's called

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withResult(null);
  }

  public void testSupplyAsync_thenRunShouldFail_WhenThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<Void> future = supplyAsyncWithResult("a")
        .thenRun(() -> {
          throw taskException;
        });

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_thenRunShouldFail_WhenFutureThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<Void> future = supplyAsyncWithException(taskException)
        .thenRun(() -> fail(getTag() + "should not be called"));

    // TODO: also check original future result?

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_thenCombine() {
    CompletableFuture<String> other = supplyAsyncWithResult("b");
    CompletableFuture<String> future = supplyAsyncWithResult("a")
        .thenCombine(other, (v1, v2) -> v1 + v2);

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("ab");
  }

  public void testSupplyAsync_thenCombineWithCompletedFuture() {
    CompletableFuture<String> other = CompletableFuture.completedFuture("b");
    CompletableFuture<String> future = supplyAsyncWithResult("a")
        .thenCombine(other, (v1, v2) -> v1 + v2);

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("ab");
  }

  public void testSupplyAsync_CompletedFutureThenCombine() {
    CompletableFuture<String> other = supplyAsyncWithResult("b");
    CompletableFuture<String> future = CompletableFuture.completedFuture("a")
        .thenCombine(other, (v1, v2) -> v1 + v2);

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("ab");
  }

  public void testSupplyAsync_thenCombineShouldComplete_WhenCompletedFutures() {
    CompletableFuture<String> other = CompletableFuture.completedFuture("b");
    CompletableFuture<String> future = CompletableFuture.completedFuture("a")
        .thenCombine(other, (v1, v2) -> v1 + v2);

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("ab");
  }

  public void testSupplyAsync_thenCombineShouldCompleteExceptionally_WhenFirstFutureThrowsException() {
    CompletableFuture<String> other = supplyAsyncWithResult("b");
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<String> future = supplyAsyncWithException(taskException)
        .thenCombine(other, (v1, v2) -> {
          fail(getTag() + "should not be called");
          return null;
        });

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_thenCombineShouldCompleteExceptionally_WhenOtherFutureThrowsException() {
    RuntimeException taskException = new RuntimeException();
    CompletableFuture<String> other = supplyAsyncWithException(taskException);
    CompletableFuture<String> future = CompletableFuture.completedFuture("a")
        .thenCombine(other, (v1, v2) -> {
          fail(getTag() + "should not be called");
          return null;
        });

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withException(new CompletionException(taskException));
  }

  public void testSupplyAsync_thenAcceptBoth() {
    CompletableFuture<String> other = supplyAsyncWithResult("b");
    CompletableFuture<Void> future = supplyAsyncWithResult("a")
        .thenAcceptBoth(other, (v1, v2) -> {
          assertEquals("a", v1);
          assertEquals("b", v2);
        });

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult(null);
  }

  public void testSupplyAsync_runAfterBoth() {
    CompletableFuture<String> other = supplyAsyncWithResult("b");
    CompletableFuture<Void> future = supplyAsyncWithResult("a")
        .runAfterBoth(other, () -> {}); // TODO: test that it's called

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult(null);
  }

  public void testSupplyAsync_applyToEither() {
    CompletableFuture<String> other = supplyAsyncWithResult("b");
    CompletableFuture<String> future = supplyAsyncWithResult("a", LONGER_DELAY)
        .applyToEither(other, v -> {
          assertEquals("b", v);
          return v;
        });

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("b");
  }

  public void testSupplyAsync_completedFutureApplyToEither() {
    CompletableFuture<String> other = CompletableFuture.completedFuture("b");
    CompletableFuture<String> future = supplyAsyncWithResult("a")
        .applyToEither(other, v -> {
          assertEquals("b", v);
          return v;
        });

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("b");
  }

  public void testSupplyAsync_acceptEither() {
    CompletableFuture<String> other = supplyAsyncWithResult("b");
    CompletableFuture<Void> future = supplyAsyncWithResult("a", LONGER_DELAY)
        .acceptEither(other, v -> assertEquals("b", v));

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult(null);
  }

  public void testSupplyAsync_runAfterEither() {
    CompletableFuture<String> other = supplyAsyncWithResult("b");
    CompletableFuture<Void> future = supplyAsyncWithResult("a", LONGER_DELAY)
        .runAfterEither(other, () -> {});// TODO: test that it's called

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult(null);
  }

  public void testSupplyAsync_thenCompose() {
    CompletableFuture<String> future = supplyAsyncWithResult("a")
        .thenCompose(v -> CompletableFuture.completedFuture(v));// TODO: rewrite the test

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("a");
  }

  public void testSupplyAsync_handle() {
    CompletableFuture<String> future = supplyAsyncWithResult("a")
        .handle((r, e) -> {
          assertEquals("a", r);
          assertNull(e);
          return r + "b";
        });

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("ab");
  }

  public void testSupplyAsync_whenComplete() {
    CompletableFuture<String> future = supplyAsyncWithResult("a")
        .whenComplete((r, e) -> {
          assertEquals("a", r);
          assertNull(e);
        });

    // TODO: also check original futures result?

    assertThat(future)
        .willComplete()
        .withResult("a");
  }

  public void testSupplyAsync_exceptionally() {
    Object result = new Object();
    CompletableFuture<Object> future = supplyAsyncWithException(new RuntimeException())
        .exceptionally(e -> result);

    assertThat(future)
        .willComplete()
        .withResult(result);
  }

  public void testCompletedFuture_ShouldBeCompletedWithResult() {
    Object result = new Object();
    CompletableFuture<Object> future = CompletableFuture.completedFuture(result);

    assertThat(future)
        .completed()
        .withResult(result);
  }

  public void testCompletedFuture_ShouldCompleteWithResult_WhenComplete() {
    Object result = new Object();
    CompletableFuture<Object> future = CompletableFuture.completedFuture(result);

    assertThat(future)
        .willComplete()
        .withResult(result);
  }

  public void testCompletedFuture_ShouldBeCompletedWithResult2_WhenCalledObtrudeResult2() {
    Object result1 = new Object();
    CompletableFuture<Object> future = CompletableFuture.completedFuture(result1);

    Object result2 = new Object();
    future.obtrudeValue(result2);

    assertThat(future)
        .completed()
        .withResult(result2);
  }

  public void testCompletedFuture_ShouldBeCompletedWithResult_WhenCalledObtrudeException() {
    CompletableFuture<Object> future = CompletableFuture.completedFuture(new Object());

    Exception taskException = new Exception();
    future.obtrudeException(taskException);

    assertThat(future)
        .completed()
        .withException(new ExecutionException(taskException));
  }

  public void testAllOf_ShouldComplete_WhenEmpty() throws Exception {
    assertThat(CompletableFuture.allOf())
        .willComplete()
        .withResult(null);
  }

  public void testAllOf_ShouldBeCompleted_WhenEmpty() throws Exception {
    assertThat(CompletableFuture.allOf())
        .willComplete()
        .withResult(null);
  }

  public void testAllOf_ShouldComplete_WhenCalledWithFutures() {
    Object result1 = new Object();
    CompletableFuture<Object> future1 = supplyAsyncWithResult(result1, LONGEST_DELAY);
    Object result2 = new Object();
    CompletableFuture<Object> future2 = supplyAsyncWithResult(result2, LONGER_DELAY);
    Object result3 = new Object();
    CompletableFuture<Object> future3 = supplyAsyncWithResult(result3, SHORT_DELAY);

    assertThat(CompletableFuture.allOf(future1, future2, future3))
        .willComplete()
        .withResult(null);
  }

  public void testAllOf_ShouldCompleteExceptionally_WhenFutureCompletesExceptionally() {
    Object result1 = new Object();
    CompletableFuture<Object> future1 = supplyAsyncWithResult(result1, LONGER_DELAY);
    Object result2 = new Object();
    CompletableFuture<Object> future2 = supplyAsyncWithResult(result2, LONGER_DELAY);
    RuntimeException failure = new RuntimeException();
    CompletableFuture<Void> future3 = runAsyncWithException(failure);

    assertThat(CompletableFuture.allOf(future1, future2, future3))
        .willComplete()
        .withException(new CompletionException(failure));
  }

  public void testAllOf_ShouldCompleteExceptionally_WhenFutureCancelled() {
    Object result1 = new Object();
    CompletableFuture<Object> future1 = supplyAsyncWithResult(result1, LONGER_DELAY);
    Object result2 = new Object();
    CompletableFuture<Object> future2 = supplyAsyncWithResult(result2, LONGER_DELAY);
    Object result3 = new Object();
    CompletableFuture<Object> future3 = supplyAsyncWithResult(result3, LONGER_DELAY);

    assertThat(CompletableFuture.allOf(future1, future2, future3))
        .willComplete()
        .withException(new CompletionException(new CancellationException()));

    future2.cancel(true);
  }

  public void testAllOf_ShouldCompleteExceptionally_WhenCancelled() {
    Object result1 = new Object();
    CompletableFuture<Object> future1 = supplyAsyncWithResult(result1, LONGER_DELAY);
    Object result2 = new Object();
    CompletableFuture<Object> future2 = supplyAsyncWithResult(result2, LONGER_DELAY);
    Object result3 = new Object();
    CompletableFuture<Object> future3 = supplyAsyncWithResult(result3, LONGER_DELAY);
    CompletableFuture<Void> future = CompletableFuture.allOf(future1, future2, future3);

    assertThat(future)
        .willComplete()
        .withException(new CancellationException());

    future.cancel(true);
  }

  public void testAllOf_ShouldBeCompleted_WhenCalledWithCompletedFutures() {
    CompletableFuture<Void> future = CompletableFuture.allOf(
        CompletableFuture.completedFuture(1),
        CompletableFuture.completedFuture(2),
        CompletableFuture.completedFuture(3));

    assertThat(future)
        .completed()
        .withResult(null);
  }

  public void testAllOf_ShouldBeCompleted_WhenCalledWithExceptionallyCompletedFuture() {
    CompletableFuture<?> exceptionallyCompleted = runAsync();
    RuntimeException taskException = new RuntimeException();
    exceptionallyCompleted.completeExceptionally(taskException);
    CompletableFuture<Void> future = CompletableFuture.allOf(
        exceptionallyCompleted,
        CompletableFuture.completedFuture(3));

    assertThat(future)
        .completed()
        .withException(new ExecutionException(taskException));
  }

  public void testAllOf_ShouldComplete_WhenContainsCompletedFuture() {
    CompletableFuture<Void> future = CompletableFuture.allOf(
        CompletableFuture.completedFuture(1),
        supplyAsyncWithResult(2));

    assertThat(future)
        .willComplete()
        .withResult(null);
  }

  public void testAnyOf_ShouldBeIncomplete_WhenEmpty() {
    CompletableFuture<Object> future = CompletableFuture.anyOf();
    assertFalse(future.isDone());
    finishTest();
  }

  public void testAnyOf_ShouldComplete_WhenCalledWithFutures() {
    Object result1 = new Object();
    CompletableFuture<Object> future1 = supplyAsyncWithResult(result1, LONGEST_DELAY);
    Object result2 = new Object();
    CompletableFuture<Object> future2 = supplyAsyncWithResult(result2, LONGER_DELAY);
    Object result3 = new Object();
    CompletableFuture<Object> future3 = supplyAsyncWithResult(result3, SHORT_DELAY);

    assertThat(CompletableFuture.anyOf(future1, future2, future3))
        .willComplete()
        .withResult(result3);
  }

  public void testAnyOf_ShouldCompleteExceptionally_WhenFutureCompletesExceptionally() {
    Object result1 = new Object();
    CompletableFuture<Object> future1 = supplyAsyncWithResult(result1, LONGER_DELAY);
    Object result2 = new Object();
    CompletableFuture<Object> future2 = supplyAsyncWithResult(result2, LONGER_DELAY);
    RuntimeException failure = new RuntimeException();
    CompletableFuture<Void> future3 = runAsyncWithException(failure);

    assertThat(CompletableFuture.anyOf(future1, future2, future3))
        .willComplete()
        .withException(new CompletionException(failure));
  }

  public void testAnyOf_ShouldCompleteExceptionally_WhenFutureCancelled() {
    Object result1 = new Object();
    CompletableFuture<Object> future1 = supplyAsyncWithResult(result1, LONGER_DELAY);
    Object result2 = new Object();
    CompletableFuture<Object> future2 = supplyAsyncWithResult(result2, LONGER_DELAY);
    Object result3 = new Object();
    CompletableFuture<Object> future3 = supplyAsyncWithResult(result3, LONGER_DELAY);

    delay(() -> future2.cancel(true), SHORT_DELAY);

    assertThat(CompletableFuture.anyOf(future1, future2, future3))
        .willComplete()
        .withException(new CompletionException(new CancellationException()));
  }

  public void testAnyOf_ShouldCompleteExceptionally_WhenCancelled() {
    Object result1 = new Object();
    CompletableFuture<Object> future1 = supplyAsyncWithResult(result1, LONGER_DELAY);
    Object result2 = new Object();
    CompletableFuture<Object> future2 = supplyAsyncWithResult(result2, LONGER_DELAY);
    Object result3 = new Object();
    CompletableFuture<Object> future3 = supplyAsyncWithResult(result3, LONGER_DELAY);
    CompletableFuture<Object> future = CompletableFuture.anyOf(future1, future2, future3);

    assertThat(future)
        .willComplete()
        .withException(new CancellationException());

    future.cancel(true);
  }

  public void testAnyOf_ShouldBeCompleted_WhenCalledWithCompletedFutures() {
    CompletableFuture<Object> future = CompletableFuture.anyOf(
        CompletableFuture.completedFuture(1),
        CompletableFuture.completedFuture(2),
        CompletableFuture.completedFuture(3)
    );

    assertThat(future)
        .completed()
        .withResult(1);
  }

  public void testAnyOf_ShouldComplete_WhenContainsCompletedFuture() {
    CompletableFuture<Object> future = CompletableFuture.anyOf(
        CompletableFuture.completedFuture(1),
        supplyAsyncWithResult(2));

    assertThat(future)
        .willComplete()
        .withResult(1);
  }

  public void testAnyOf_ShouldBeCompleted_WhenContainsCompletedFuture() {
    CompletableFuture<Object> future = CompletableFuture.anyOf(
        CompletableFuture.completedFuture(1),
        supplyAsyncWithResult(2));

    assertThat(future)
        .completed()
        .withResult(1);
  }

  public void testAnyOf_ShouldBeCompleted_WhenCalledWithExceptionallyCompletedFuture() {
    CompletableFuture<?> exceptionallyCompleted = runAsync();
    RuntimeException taskException = new RuntimeException();
    exceptionallyCompleted.completeExceptionally(taskException);
    CompletableFuture<Object> future = CompletableFuture.anyOf(
        supplyAsyncWithResult(new Object()),
        exceptionallyCompleted,
        CompletableFuture.completedFuture(3));

    assertThat(future)
        .completed()
        .withException(new ExecutionException(taskException));
  }

  private FutureSubject assertThat(CompletableFuture<?> future) {
    return new FutureSubject(future);
  }

  private class FutureSubject {
    private final CompletableFuture<?> future;

    public FutureSubject(CompletableFuture<?> future) {
      this.future = future;
    }

    public IncompleteFutureSubject willComplete() {
      return new IncompleteFutureSubject(future);
    }

    public CompletedFutureSubject completed() {
      return new CompletedFutureSubject(future);
    }
  }

  private class CompletedFutureSubject {
    private final CompletableFuture<?> future;

    public CompletedFutureSubject(CompletableFuture<?> future) {
      this.future = future;
    }

    public void withResult(Object expected) {
      assertCompletedSuccessfully(future);
      // TODO: use whenComplete?
      try {
        assertEquals(getTag() + "future has different result", expected, future.get());
      } catch (InterruptedException | ExecutionException e) {
        fail(getTag() + "failed to get completed future result: " + e.getMessage());
      }
      // TODO
      finishTest();
    }

    public void withException(Throwable expected) {
      assertCompletedExceptionally(future);
      // TODO: use whenComplete?
      try {
        Object result = future.get();
        fail(getTag() + "future has been completed exceptionally but has result: " + result);
      } catch (InterruptedException e) {
        fail(getTag() + "failed to get completed future result: " + e.getMessage());
      } catch (ExecutionException e) {
        assertEquals(getTag() + "failed future should have thrown " + expected +
            " but threw " + e, expected, e);
      }
      // TODO
      finishTest();
    }
  }

  private class IncompleteFutureSubject {
    private final CompletableFuture<?> future;

    public IncompleteFutureSubject(CompletableFuture<?> future) {
      this.future = future;
    }

    public void withResult(Object expected) {
      future.whenComplete((r, e) -> {
        assertCompletedWithResult(future, expected, r, e);
        finishTest();
      });
    }

    public void withException(Throwable expected) {
      future.whenComplete((r, e) -> {
        assertFailed(future, expected, r, e);
        finishTest();
      });
    }
  }

  private static final int SHORT_DELAY = 100;
  private static final int LONGER_DELAY = 200;
  private static final int LONGEST_DELAY = 300;

  private static final int TEST_FINISH_DELAY = 10000;

  @Override
  protected void gwtSetUp() throws Exception {
    delayTestFinish(TEST_FINISH_DELAY);
  }

  private static <T> CompletableFuture<T> supplyAsyncWithResult(T result) {
    return supplyAsyncWithResult(result, SHORT_DELAY);
  }

  private static <T> CompletableFuture<T> supplyAsyncWithResult(T result, int delay) {
    return CompletableFuture.supplyAsync(() -> result, delayExecutionBy(delay));
  }

  private static <T> CompletableFuture<T> supplyAsyncWithException(RuntimeException e) {
    return supplyAsyncWithException(e, SHORT_DELAY);
  }

  private static <T> CompletableFuture<T> supplyAsyncWithException(RuntimeException e, int delay) {
    return CompletableFuture.supplyAsync(() -> {
      throw e;
    }, delayExecutionBy(delay));
  }

  private static CompletableFuture<Void> runAsync() {
    return runAsync(SHORT_DELAY);
  }

  private static CompletableFuture<Void> runAsync(int delay) {
    return CompletableFuture.runAsync(() -> {}, delayExecutionBy(delay));
  }

  private static CompletableFuture<Void> runAsyncWithException(RuntimeException e) {
    return runAsyncWithException(e, SHORT_DELAY);
  }

  private static CompletableFuture<Void> runAsyncWithException(RuntimeException e, int delay) {
    return CompletableFuture.runAsync(() -> {
      throw e;
    }, delayExecutionBy(delay));
  }

  private void assertCompletedWithResult(CompletableFuture<?> future, Object expected,
      Object actual, Throwable e) {
    assertCompletedSuccessfully(future);
    // TODO: assertSame?
    assertEquals(getTag() + "future should have returned " + expected + " but it returned "
        + actual + " instead", expected, actual);
    assertNull(getTag() + "future should not have thrown exception but it threw " + e, e);
  }

  private void assertFailed(CompletableFuture<?> future, Throwable expected,
      Object r, Throwable actual) {

    assertTrue(getTag() + "failed future should have been completed", future.isDone());
    assertTrue(getTag() + "failed future should have been completed exceptionally",
        future.isCompletedExceptionally());
    assertNull(getTag() + "failed future should have no result", r);
    assertEquals(getTag() + "failed future should have thrown " + expected + " but threw " + actual,
        expected, actual);
  }

  private static void assertEquals(String message, Throwable expected, Throwable actual) {
    if (expected == null) {
      assertNull(message, actual);
      return;
    }

    assertNotNull(message, actual);
    if (expected instanceof CancellationException
        || expected instanceof CompletionException
        || expected instanceof ExecutionException) {
      assertEquals(message, expected.getClass(), actual.getClass());
      assertEquals(message, expected.getMessage(), actual.getMessage());
      assertEquals(message, expected.getCause(), actual.getCause());
    } else {
      assertEquals(message, (Object) expected, (Object) actual);
    }
  }

  private void assertCompletedSuccessfully(CompletableFuture<?> future) {
    assertTrue(getTag() + "future should have been completed", future.isDone());
    assertFalse(getTag() + "future should not have been cancelled", future.isCancelled());
    assertFalse(getTag() + "future should not have been completed exceptionally",
        future.isCompletedExceptionally());
  }

  private void assertCompletedExceptionally(CompletableFuture<?> future) {
    assertTrue(getTag() + "future should have been completed", future.isDone());
    assertFalse(getTag() + "future should not have been cancelled", future.isCancelled());
    assertTrue(getTag() + "future should have been completed exceptionally",
        future.isCompletedExceptionally());
  }

  private String getTag() {
    return "[" + getName() + "] ";
  }

  private static Executor delayExecutionBy(final int millis) {
    return new Executor() {
      @Override
      public void execute(Runnable command) {
        delay(command, millis);
      }
    };
  }

  private static void delay(final Runnable runnable, int delay) {
    Timer timer = new Timer() {
      @Override
      public void run() {
        runnable.run();
      }
    };
    timer.schedule(delay);
  }
}
