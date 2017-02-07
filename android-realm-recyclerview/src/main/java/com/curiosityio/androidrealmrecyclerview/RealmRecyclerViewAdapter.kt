package com.curiosityio.androidrealmrecyclerview

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import difflib.Delta
import difflib.DiffUtils
import io.realm.*
import io.realm.RealmRecyclerViewAdapter
import java.util.*

// Abstract RV adapter for Realm. Use it just like a regular RV adapter, but with a couple added features.
// it will automatically insert and delete rows (with animations) for RV when data changes to the parent model.
// Changes to RV rows is not automatic. Must call setPendingItemChanged() before you perform the realm write transaction in order to have notifyDataSetChanged called for you.
// with this adapter, you should not be manually calling any notify_() functions. They should all be in here for you!
abstract class RealmRecyclerViewAdapter<PARENT_MODEL: RealmObject, LIST_DATA: RealmObject, VH: RecyclerView.ViewHolder>(parentContext: Context, val parentModel: PARENT_MODEL, val getDataInterface: GetDataInterface<LIST_DATA>) : RealmRecyclerViewAdapter<LIST_DATA, VH>(parentContext, getDataInterface.getRealmList(), false) {

    interface GetDataInterface<LIST_DATA: RealmObject> {
        fun getRealmList(): RealmList<LIST_DATA>?
        fun getIdForListItem(item: LIST_DATA): Any
    }

    private var dataListIds: ArrayList<Any> = ArrayList()
    private var parentModelChangeListener: RealmChangeListener<PARENT_MODEL>

    private class PendingItemChange<LIST_DATA: RealmModel>(val pendingItemChangeId: Any, val pendingItemChangedDidChangeListener: (LIST_DATA) -> Boolean)

    private val pendingApiChanges: ArrayList<PendingItemChange<LIST_DATA>> = ArrayList()
    // because we dont want to notifyDataSetChanged() on every change to the data set (there may be too much updating to the realm and it gets noisy. All previously focused views get unfocused.) if there is an item you want to call notifyItemChanged() on, you call this function, and return true from didChange() whenever you found the update you applied to the model. Then, adapter will call notifyItemChanged() for you.
    protected fun setPendingItemChanged(id: Any, didChange: (LIST_DATA) -> Boolean) {
        if (!pendingApiChanges.containsWhere { it.pendingItemChangeId == id }) {
            pendingApiChanges.add(PendingItemChange(id, didChange))
        }
    }

    init {
        getDataInterface.getRealmList()?.forEach { listItem ->
            dataListIds.add(getDataInterface.getIdForListItem(listItem))
        }

        parentModelChangeListener = RealmChangeListener<PARENT_MODEL> { parentModel ->
            val newDataListIds: ArrayList<Any> = ArrayList()
            getDataInterface.getRealmList()?.forEachIndexed { index, listItem ->
                val idForListItem = getDataInterface.getIdForListItem(listItem)

                newDataListIds.add(idForListItem)

                pendingApiChanges.find { it.pendingItemChangeId == idForListItem }?.let {
                    if (it.pendingItemChangedDidChangeListener.invoke(listItem)) {
                        pendingApiChanges.remove(it)

                        Handler().postDelayed({
                            notifyDataSetChanged()
                        }, 100)
                    }
                }
            }

            val deltas = DiffUtils.diff(dataListIds, newDataListIds).deltas
            dataListIds = newDataListIds
            deltas.forEach { delta ->
                if (delta.type == Delta.TYPE.INSERT) {
                    notifyItemRangeInserted(delta.revised.position, delta.revised.size())
                } else if (delta.type == Delta.TYPE.DELETE) {
                    notifyItemRangeRemoved(delta.original.position, delta.original.size())
                }
            }
        }

        parentModel.addChangeListener(parentModelChangeListener)
    }

    // Make sure to call this when your Realm UI instance is closed. It will remove change listener on models here.
    open fun close() {
        parentModel.removeChangeListener(parentModelChangeListener)
    }

    override fun getItemCount(): Int = getDataInterface.getRealmList()?.count() ?: 0

}

fun <E> Collection<E>.containsWhere(predicate: (E) -> Boolean): Boolean {
    return find(predicate) != null
}