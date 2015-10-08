package co.moreoptions.shopping.core;

import java.util.ArrayList;
import java.util.List;

import co.moreoptions.shopping.core.models.request.Values;
import co.moreoptions.shopping.core.models.request.ValuesBatchModel;

/**
 * Created by 11101 on 17/09/15.
 */
public class ValueBatchHelper {
    private ValuesBatchModel mValueBatchModel = new ValuesBatchModel();
    private boolean isBatching;
    private boolean shouldBatch = true;

    public void ValuesBatchModel(){
    }

    public boolean isBatching() {
        return isBatching;
    }

    public void setShouldBatch(boolean shouldBatch) {
        this.shouldBatch = shouldBatch;
    }

    public boolean getShouldbatch(){
        return shouldBatch;
    }

    public void setIsBatching(boolean isBatching) {
        this.isBatching = isBatching;
    }

    public boolean checkForBatchStart(String viewId){
        if(viewId != null && viewId.contains("product_page_title_main_title") && getShouldbatch()){
            setIsBatching(true);
            return isBatching;
        }
        else {
            return false;
        }
    }

    public void addValue(String appName, String value){
        mValueBatchModel.setAppName(appName);
        mValueBatchModel.setProductName(value);
    }

    public ValuesBatchModel getFinalRequestBody(){
        return mValueBatchModel;
    }

    public void clearBatch(){
        setIsBatching(false);
        mValueBatchModel = new ValuesBatchModel();
    }
}
