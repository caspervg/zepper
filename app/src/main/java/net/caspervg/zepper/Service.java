package net.caspervg.zepper;

import android.os.AsyncTask;

public abstract class Service<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private Task<Params, Result> task;
    private Executable<Result> postSuccess;
    private Executable<Result> postFailure;
    private Executable<Result> postFinish;
    private Executable<Void> cancelExecutable;
    private Executable<Void> preExecutable;
    private Updateable<Progress> progressUpdateable;

    @Override
    protected Result doInBackground(Params... paramses) {
        try {
            return task.assign(paramses);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Result result) {
        if (result != null && postSuccess != null) {
            postSuccess.execute(result);
        } else if (postFailure != null) {
            postFailure.execute(result);
        }

        if (postFinish != null) {
            postFinish.execute(result);
        }
    }

    @Override
    protected void onCancelled() {
        if (cancelExecutable != null) {
            cancelExecutable.execute(null);
        }
    }

    @Override
    protected void onPreExecute() {
        if (preExecutable != null) {
            preExecutable.execute(null);
        }
    }

    @Override
    protected void onProgressUpdate(Progress... progresses) {
        if (progressUpdateable != null) {
            progressUpdateable.update(progresses);
        }
    }

    protected abstract Task<Params, Result> createTask();

    public void setOnSuccess(Executable<Result> executable) {
        this.postSuccess = executable;
    }

    public void setOnFailure(Executable<Result> executable) {
        this.postFailure = executable;
    }

    public void setOnFinished(Executable<Result> executable) {
        this.postFinish = executable;
    }

    public void setOnUpdate(Updateable<Progress> updateable) {
        this.progressUpdateable = updateable;
    }

    public void setOnCancelled(Executable<Void> executable) {
        this.cancelExecutable = executable;
    }

    public void setOnPrepare(Executable<Void> executable) {
        this.preExecutable = executable;
    }

    public void start(Params... paramses) {
        this.task = createTask();
        this.execute(paramses);

    }
}
