package ms.com.extensionandlib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class BaseRecyclerAdapter<T>(arrayData: MutableList<T>, @LayoutRes layout: Int, val screenWith: Int?) :
    RecyclerView.Adapter<BaseRecyclerAdapter<T>.BaseRecyclerViewHolder>() {

    var _arrayData = arrayData
    val layout = layout
    var bindEvent: ((View, T?, Int?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BaseRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        if (screenWith != null) {
            view.layoutParams = RecyclerView.LayoutParams(screenWith, RecyclerView.LayoutParams.WRAP_CONTENT)
        }
        return BaseRecyclerViewHolder(view)
    }

    fun updateRecycler(data: MutableList<T>) {
        _arrayData.addAll(data)
        notifyDataSetChanged()
    }

    fun clearRecycler() {
        _arrayData.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return _arrayData.size
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, postiton: Int) {
        holder.bindItem(_arrayData.getOrNull(postiton))
    }

    @CallSuper
    open fun setUpView(itemView: View?, holder: BaseRecyclerViewHolder) {

    }

    inner class BaseRecyclerViewHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView) {
            setUpView(itemView, this)
        }
        fun bindItem(item: T?) {
            item.let { bindEvent?.invoke(itemView, it, adapterPosition) }
        }

    }
}
