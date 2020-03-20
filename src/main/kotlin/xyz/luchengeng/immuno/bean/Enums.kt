package xyz.luchengeng.immuno.bean
interface Enumerable{
    val value : Int
}
enum class Gender(override val value: Int) : Enumerable {
    MALE(1),
    FEMALE(2);
    companion object {
        private val map = values().associateBy(Gender::value)
        fun fromInt(type: Int?) = map[type]
    }
}

enum class TNM(override val value : Int) : Enumerable{
    STAGE1(1),
    STAGE2(2),
    STAGE3(3),
    STAGE4(4);
    companion object {
        private val map = values().associateBy(TNM::value)
        fun fromInt(type: Int?) = map[type]
    }
}

enum class Race(override val value : Int) : Enumerable{
    WHITE(1),
    ASIAN(2),
    BLACK(3);
    companion object {
        private val map = values().associateBy(Race::value)
        fun fromInt(type: Int?) = map[type]
    }
}