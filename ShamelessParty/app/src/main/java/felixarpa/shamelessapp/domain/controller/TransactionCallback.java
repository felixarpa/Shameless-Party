package felixarpa.shamelessapp.domain.controller;

public interface TransactionCallback {
    void onSuccess();
    void onFailure(String message);
}
