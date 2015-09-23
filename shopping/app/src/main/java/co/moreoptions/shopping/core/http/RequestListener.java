package co.moreoptions.shopping.core.http;

/**
 * Created by anshul on 17/09/15.
 */
public interface RequestListener<RESULT> {
    void onRequestFailure(Exception e);
    void onRequestSuccess(RESULT jsonObject);
}

