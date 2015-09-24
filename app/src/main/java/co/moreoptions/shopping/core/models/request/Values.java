package co.moreoptions.shopping.core.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anshul on 17/09/15.
 */
public class Values {
    @SerializedName("id")
    public String id;
    @SerializedName("value")
    public String value;
    //TODO waiting for the serve support
//    @SerializedName("seqNum")
//    public String seqNum;

    public void setId(String id) {
        this.id = id;
    }

//    public void setSeqNum(String seqNum) {
//        this.seqNum = seqNum;
//    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Values that = (Values) o;

        return com.google.common.base.Objects.equal(this.id, that.id) &&
                com.google.common.base.Objects.equal(this.value, that.value);
//        &&
//                com.google.common.base.Objects.equal(this.seqNum, that.seqNum);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(id, value);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("id", id)
                .add("value", value)
                //.add("seqNum", seqNum)
                .toString();
    }
}
