package net.caspervg.zepper;

public interface Updateable<Progress> {
    public void update(Progress... progresses);
}
