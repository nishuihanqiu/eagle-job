package com.lls.api.eagle.enums;

/************************************
 * FutureState
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public enum FutureState {

    DOING(0),
    DONE(1),
    CANCELLED(2);

    public final int value;

    private FutureState(int value) {
        this.value = value;
    }

    public boolean isCancelledState() {
        return this == CANCELLED;
    }

    public boolean isDoneState() {
        return this == DONE;
    }

    public boolean isDoingState() {
        return this == DOING;
    }


}
