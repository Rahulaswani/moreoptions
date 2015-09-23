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
    private List<Values> valuesList = new ArrayList<>();

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

    public void addValues(String appName, String id, String value, String index){
        if(isBatching) {

            if(id == null) {
                return;
            }

            String[] splitString = id.split("/");
            mValueBatchModel.setAppName(appName);
            Values values = new Values();
            if(splitString[1] != null) {
                values.setId(splitString[1]);
            }
            else {
                values.setId("");
            }
            //values.setSeqNum(index);
            values.setValue(value);
            valuesList.add(values);
        }
    }

    public ValuesBatchModel getFinalRequestBody(){
        mValueBatchModel.setValues(valuesList);
        return mValueBatchModel;
    }

    public void clearBatch(){
        setIsBatching(false);
        valuesList.clear();
        mValueBatchModel = new ValuesBatchModel();
    }
}
