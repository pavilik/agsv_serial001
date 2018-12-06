package com.felhr.serialagsv;

class SignalizerItem {
    private Integer numberSignalizer; //номер сигнализатора
    private boolean visibleStaus; // видимость сигнализатора
    private Integer conditionCode; // код состояния ....дополнить при анализе протокола

    public SignalizerItem (Integer setNumberSignalizer, Boolean setVisibleStatus, Integer setConditionCode)
    {
        numberSignalizer = setNumberSignalizer;
        visibleStaus = setVisibleStatus;
        conditionCode = setConditionCode;
    }

    public Integer getNumberSignalizer() {
        return numberSignalizer;
    }

    public void setNumberSignalizer(Integer numberSignalizer) {
        this.numberSignalizer = numberSignalizer;
    }

    public boolean isVisibleStaus() {
        return visibleStaus;
    }

    public void setVisibleStaus(boolean visibleStaus) {
        this.visibleStaus = visibleStaus;
    }

    public Integer getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(Integer conditionCode) {
        this.conditionCode = conditionCode;
    }
}
