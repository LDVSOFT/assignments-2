package ru.spbau.mit.servers;

/**
 * Created by ldvsoft on 29.04.16.
 */
abstract class SynchronousServer extends ServerBase {
    protected abstract void submit(Runnable task);
    protected abstract void work();

    @Override
    public final void run() {
        submit(this::work);
    }


}
