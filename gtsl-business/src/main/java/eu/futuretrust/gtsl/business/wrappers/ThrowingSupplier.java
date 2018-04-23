package eu.futuretrust.gtsl.business.wrappers;

@FunctionalInterface
public interface ThrowingSupplier<R, E extends Throwable> {

  R get() throws E;

}
