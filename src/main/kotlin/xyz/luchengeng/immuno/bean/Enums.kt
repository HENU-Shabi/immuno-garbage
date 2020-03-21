package xyz.luchengeng.immuno.bean
enum class Gender(val value: Int) {
    MALE(1),
    FEMALE(2);
    companion object {
        private val map = values().associateBy(Gender::value)
        fun fromInt(type: Int?) = map[type]
    }
}

enum class TNM(val value : Int){
    STAGE1(1),
    STAGE2(2),
    STAGE3(3),
    STAGE4(4);
    companion object {
        private val map = values().associateBy(TNM::value)
        fun fromInt(type: Int?) = map[type]
    }
}

enum class Race(val value : Int){
    WHITE(1),
    ASIAN(2),
    BLACK(3);
    companion object {
        private val map = values().associateBy(Race::value)
        fun fromInt(type: Int?) = map[type]
    }
}