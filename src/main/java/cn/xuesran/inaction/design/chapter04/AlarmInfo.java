package cn.xuesran.inaction.design.chapter04;

import cn.xuesran.inaction.design.chapter05.example.AlarmType;

public class AlarmInfo {


    private String id;

    private AlarmType type;

    private String extraInfo;

    /**
     * 获取extraInfo
     *
     * @return extraInfo
     */
    public String getExtraInfo() {
        return extraInfo;
    }

    /**
     * 设置extraInfo
     *
     * @param extraInfo extraInfo
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    /**
     * 获取id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取type
     *
     * @return type
     */
    public AlarmType getType() {
        return type;
    }

    /**
     * 设置type
     *
     * @param type type
     */
    public void setType(AlarmType type) {
        this.type = type;
    }

    public static final class AlarmInfoBuilder {
        private AlarmType type;

        private AlarmInfoBuilder() {
        }

        public static AlarmInfoBuilder anAlarmInfo() {
            return new AlarmInfoBuilder();
        }

        public AlarmInfoBuilder withType(AlarmType type) {
            this.type = type;
            return this;
        }

        public AlarmInfo build() {
            AlarmInfo alarmInfo = new AlarmInfo();
            alarmInfo.type = this.type;
            return alarmInfo;
        }
    }
}
