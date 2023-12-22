package com.example.dummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vegetables(
    val name: String,
    val photo: Int,
    val price: String,
    val kandungan: String,
    val detail: String
): Parcelable

//object DummyData {
//    val VegetablesList: List<Vegetables>
//        get() = listOf(
//            Vegetables(
//                1,
//                "Brokoli",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Brokoli merupakan sumber nutrisi yang kaya, termasuk vitamin K, vitamin C, serat, serta senyawa antioksidan yang bisa membantu melindungi tubuh dari berbagai penyakit."
//            ),Vegetables(
//                2,
//                "Bayam",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Bayam merupakan salah satu jenis sayuran yang cukup populer di Indonesia. Sayur yang memiliki nama lain Amaranthus ini, seringkali diolah menjadi berbagai macam menu makanan, seperti lalapan, pecel hingga aneka tumisan."
//            ),Vegetables(
//                3,
//                "Jagung",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Jagung Merupakan sumber energi yang baik karena kandungan karbohidratnya. Selain itu, jagung juga mengandung serat dan beberapa nutrisi penting seperti vitamin C dan magnesium"
//            ),Vegetables(
//                4,
//                "Kacang Panjang",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Kacang panjang Ada berbagai jenis kacang, seperti kacang tanah, kacang hijau, dan lainnya. Mereka kaya akan protein nabati, serat, vitamin, mineral, dan lemak sehat"
//            ),Vegetables(
//                5,
//                "Kangkung",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Kangkung : Mengandung vitamin A, vitamin C, serta zat besi. Sayuran ini rendah kalori namun kaya akan nutrisi penting."
//            ),Vegetables(
//                6,
//                "Kentang",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Kentang merupakan sumber karbohidrat kompleks, vitamin C, vitamin B6, dan kalium. Tapi ingat, kentang cenderung memiliki indeks glikemik yang cukup tinggi"
//            ),Vegetables(
//                7,
//                "Terong",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Sayuran ini memiliki beragam warna, mulai dari ungu gelap hingga ungu terang atau putih. Kaya akan serat, rendah kalori, dan mengandung antioksidan yang baik untuk kesehatan jantung."
//            ),Vegetables(
//                8,
//                "Timun",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Timun memiliki kandungan air yang tinggi sehingga membantu menjaga tubuh tetap terhidrasi. Kaya akan vitamin K dan beberapa antioksidan."
//            ),Vegetables(
//                9,
//                "Tomat",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Tomat memiliki Sumber vitamin C, potassium, folat, dan vitamin K. Tomat juga kaya akan likopen, suatu jenis antioksidan yang berhubungan dengan penurunan risiko penyakit jantung."
//            ),Vegetables(
//                10,
//                "Wortel",
//                "Kandungan : Vitamin C, Vitamin K, Serat, Kalium, Fosfor, Zat Besi",
//                "Wortel merupakan sayuran yang Kaya akan beta-karoten yang penting untuk kesehatan mata dan sistem kekebalan tubuh. Selain itu, wortel juga mengandung serat dan vitamin K1."
//            ),
//
//            // Tambahkan data lain sesuai kebutuhan
//        )
//}