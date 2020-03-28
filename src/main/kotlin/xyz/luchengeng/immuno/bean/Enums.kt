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

enum class RadiationTherapy(val value : Int){
    TRUE(1),
    FALSE(0);
    companion object {
        private val map = RadiationTherapy.values().associateBy(RadiationTherapy::value)
        fun fromInt(type: Int?) = map[type]
    }
}

enum class Recurrence(val value : Int){
    TRUE(1),
    FALSE(0);
    companion object {
        private val map = Recurrence.values().associateBy(Recurrence::value)
        fun fromInt(type: Int?) = map[type]
    }
}

enum class Metastasis(val value : Int){
    DISTANT(1),
    LOCOREGIONAL(2);
    companion object {
        private val map = Metastasis.values().associateBy(Metastasis::value)
        fun fromInt(type: Int?) = map[type]
    }
}