package com.chicagoroboto.features.info

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chicagoroboto.R
import com.chicagoroboto.model.Library
import kotlinx.android.synthetic.main.view_info_library.view.*

class InfoAdapter(val libraryListener: (library: Library) -> Unit) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

    private val libraries: MutableMap<String, MutableList<Library>> = mutableMapOf()
    private var count = 1

    fun setLibraries(libraries: List<Library>) {
        count = 1
        this.libraries.clear()
        for (library in libraries) {
            val libs = if (this.libraries.contains(library.owner)) {
                this.libraries[library.owner]
            } else {
                this.libraries[library.owner] = mutableListOf()
                this.libraries[library.owner]
            }
            libs?.add(library)
            ++count
        }
        count += this.libraries.size
    }

    override fun getItemViewType(position: Int): Int {
        var i = position
        if (i == 0) {
            return R.layout.view_info_topper
        }
        --i
        for ((key, value) in libraries) {
            if (i == 0) {
                return R.layout.view_info_header
            }
            --i
            if (i < value.size) {
                return R.layout.view_info_library
            }
            i -= value.size
        }
        throw IllegalArgumentException("Unknown position: $position")
    }

    private fun getItem(position: Int): Library? {
        var i = position
        --i
        for ((key, value) in libraries) {
            if (i == 0) {
                return value[0]
            }
            --i
            if (i < value.size) {
                return value[i]
            }
            i -= value.size
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view, libraryListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.view_info_topper -> return
            R.layout.view_info_header, R.layout.view_info_library -> {
                val library = getItem(position)
                if (library != null) {
                    holder.bind(library)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return count
    }

    class ViewHolder(itemView: View, val libraryListener: (library: Library) -> Unit) : RecyclerView.ViewHolder(itemView) {
        var library: Library? = null
        val title: TextView?
        val license: TextView?

        init {
            title = itemView.title
            license = itemView.license
            itemView.setOnClickListener {
                if (itemViewType == R.layout.view_info_library && library != null) {
                    libraryListener(library!!)
                }
            }
        }

        fun bind(library: Library) {
            this.library = library
            when (itemViewType) {
                R.layout.view_info_header -> title?.text = library.owner
                R.layout.view_info_library -> title?.text = library.name
            }
            license?.text = library.license
        }
    }
}