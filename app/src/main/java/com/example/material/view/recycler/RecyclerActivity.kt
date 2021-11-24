package com.example.material.view.recycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.material.databinding.ActivityRecyclerBinding

class RecyclerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerBinding

    private var isNewList = false
    lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var adapter: RecyclerActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataRecycler = arrayListOf(
            Pair(DataRecycler(1, "Note", "Покормить кота!!!"), false)
        )

        dataRecycler.add(0, Pair(DataRecycler(0, "Header"), false))

        adapter = RecyclerActivityAdapter(
            object : RecyclerActivityAdapter.OnListItemClickListener {
                override fun onItemClick(dataItemClick: DataRecycler) {
                    Toast.makeText(
                        this@RecyclerActivity, dataItemClick.someText,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            dataRecycler,
            object : OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
        )

        binding.recyclerView.adapter = adapter

        binding.recyclerActivityAddNoteFAB.setOnClickListener { adapter.appendItem() }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.recyclerActivityDiffUtilFAB.setOnClickListener { changeAdapterData() }
    }

    private fun changeAdapterData() {
        adapter.setItems(createItemList(isNewList))
        isNewList = !isNewList
    }

    private fun createItemList(instanceNumber: Boolean): List<Pair<DataRecycler, Boolean>> {
        return when (instanceNumber) {
            false -> listOf(
                Pair(DataRecycler(0, "Header"), false),
                Pair(DataRecycler(1, "Note", ""), false),
                Pair(DataRecycler(2, "Note", ""), false),
                Pair(DataRecycler(3, "Note", ""), false)
            )
            true -> listOf(
                Pair(DataRecycler(0, "Header"), false),
                Pair(DataRecycler(1, "Note", "Покормить кота"), false),
                Pair(DataRecycler(2, "Note", "Приготовить макарики"), false),
                Pair(DataRecycler(3, "Note", "Сходить в магазин"), false)
            )
        }
    }
}



