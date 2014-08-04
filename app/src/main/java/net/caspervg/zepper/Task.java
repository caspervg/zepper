package net.caspervg.zepper;

public interface Task<Params, Result> {
    public Result assign(Params... paramses);
}
