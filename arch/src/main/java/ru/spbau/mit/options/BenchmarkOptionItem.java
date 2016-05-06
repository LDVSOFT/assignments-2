package ru.spbau.mit.options;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for benchmark options to create option fabric.
 *
 * Requirements:
 * 1. Can only annotate classes
 * 2. That implement BenchmarkOption interface
 * 3. That have constructor without parameters (may not be public if in current package)
 * 4. That can be instantiated (not abstract)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@interface BenchmarkOptionItem {
}
