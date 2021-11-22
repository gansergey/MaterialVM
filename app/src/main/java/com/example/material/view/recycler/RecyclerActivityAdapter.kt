package com.example.material.view.recycler

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.material.R

class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var dataRecycler: MutableList<Pair<DataRecycler, Boolean>>,

    private val dragListener: OnStartDragListener
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_NOTE -> NoteViewHolder(
                inflater.inflate(
                    R.layout.activity_recycler_item_note, parent, false
                ) as View
            )
            else -> HeaderViewHolder(
                inflater.inflate(
                    R.layout.activity_recycler_item_header, parent, false
                ) as View
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(dataRecycler[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChange =
                createCombinedPayload(payloads as List<Change<Pair<DataRecycler, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.first.someText != oldData.first.someText) {
                holder.itemView.findViewById<TextView>(R.id.noteSomeTextView).text =
                    newData.first.someText
            }
        }
    }

    override fun getItemCount(): Int {
        return dataRecycler.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            dataRecycler[position].first.someDescription.isNullOrBlank() -> TYPE_NOTE

            else -> TYPE_NOTE
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        dataRecycler.removeAt(fromPosition).apply {
            dataRecycler.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        dataRecycler.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setItems(newItems: List<Pair<DataRecycler, Boolean>>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(dataRecycler, newItems))
        dataRecycler.clear()
        dataRecycler.addAll(newItems)
        result.dispatchUpdatesTo(this)
    }

    fun appendItem() {
        dataRecycler.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() = Pair(DataRecycler(1, "Note", ""), false)

    inner class DiffUtilCallback(
        private var oldItems: List<Pair<DataRecycler, Boolean>>,
        private var newItems: List<Pair<DataRecycler, Boolean>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int):
                Boolean =
            oldItems[oldItemPosition].first.id == newItems[newItemPosition].first.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int):
                Boolean = oldItems[oldItemPosition].first.someText ==
                newItems[newItemPosition].first.someText

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int):
                Any? {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return Change(
                oldItem,
                newItem
            )
        }
    }

    inner class NoteViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {

        @SuppressLint("ClickableViewAccessibility")
        override fun bind(dataItem: Pair<DataRecycler, Boolean>) {

            itemView.findViewById<ImageView>(R.id.noteImageView).setOnClickListener {
                onListItemClickListener.onItemClick(dataItem.first)
            }
            itemView.findViewById<ImageView>(R.id.addItemImageView).setOnClickListener { addItem() }
            itemView.findViewById<ImageView>(R.id.removeItemImageView)
                .setOnClickListener { removeItem() }
            itemView.findViewById<ImageView>(R.id.moveItemDown).setOnClickListener { moveDown() }
            itemView.findViewById<ImageView>(R.id.moveItemUp).setOnClickListener { moveUp() }

            itemView.findViewById<EditText>(R.id.noteDescriptionTextView)
                .setOnClickListener { toggleText() }
            itemView.findViewById<TextView>(R.id.noteSomeTextView)
                .setOnClickListener { toggleText() }

            itemView.findViewById<ImageView>(R.id.dragHandleImageView)
                .setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this)
                    }
                    false
                }
        }

        private fun addItem() {
            dataRecycler.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun removeItem() {
            dataRecycler.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveUp() {

            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                dataRecycler.removeAt(currentPosition).apply {
                    dataRecycler.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < dataRecycler.size - 1 }?.also { currentPosition ->
                dataRecycler.removeAt(currentPosition).apply {
                    dataRecycler.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun toggleText() {
            dataRecycler[layoutPosition] = dataRecycler[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(dataItem: Pair<DataRecycler, Boolean>) {
            itemView.setOnClickListener {
                onListItemClickListener.onItemClick(dataItem.first)
            }
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(dataItemClick: DataRecycler)
    }

    companion object {
        private const val TYPE_NOTE = 0
        private const val TYPE_HEADER = 1
    }
}

data class DataRecycler(
    val id: Int = 0,
    val someText: String = "Text",
    val someDescription: String? = "Description"
)



