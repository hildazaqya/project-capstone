package com.example.foodection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.CariSayurAdapter
import com.example.dummy.Vegetables
import com.example.foodection.databinding.ActivityCariSayurBinding

class CariSayurActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCariSayurBinding
    private lateinit var rvCariSayur: RecyclerView
    private val list = ArrayList<Vegetables>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCariSayurBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvCariSayur = binding.rvCariSayur
        rvCariSayur.setHasFixedSize(true)

        list.addAll(getListCariSayur())
        showRecyclerList()
    }

    @SuppressLint("Recycle")
    private fun getListCariSayur(): ArrayList<Vegetables> {
        val dataKandungan = resources.getStringArray(R.array.data_kandungan)
        val dataPoto = resources.obtainTypedArray(R.array.data_poto)
        val dataSayur = resources.getStringArray(R.array.data_sayur)
        val dataPrice = resources.getStringArray(R.array.data_price)
        val dataDetail = resources.getStringArray(R.array.data_detail)
        val listCariSayur = ArrayList<Vegetables>()
        for (i in dataSayur.indices) {
            val vegetables = Vegetables(dataSayur[i], dataPoto.getResourceId(i, -1), dataPrice[i], dataKandungan[i], dataDetail[i])
            listCariSayur.add(vegetables)
        }
        return listCariSayur
    }

    private fun showRecyclerList() {
        rvCariSayur.layoutManager = LinearLayoutManager(this)
        val cariSayurAdapter = CariSayurAdapter(list)
        rvCariSayur.adapter = cariSayurAdapter

        cariSayurAdapter.setOnItemClickCallback(object : CariSayurAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Vegetables) {
            }
        })
    }
}