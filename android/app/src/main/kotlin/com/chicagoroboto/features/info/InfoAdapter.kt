package com.chicagoroboto.features.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chicagoroboto.R
import com.chicagoroboto.model.Library
import kotlinx.android.synthetic.main.view_info_library.view.*

class InfoAdapter(
    private val libraryListener: (library: Library) -> Unit
) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

  private var libraries: Map<String, List<Library>> = mapOf()
  private var count = 1

  fun setLibraries(libraries: List<Library>) {
    this.libraries = libraries.groupBy { it.owner }
    count = libraries.size + this.libraries.keys.size
    notifyDataSetChanged()
  }

  override fun getItemViewType(position: Int): Int {
    var i = position
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
    return R.layout.view_info_header
  }

  private fun getItem(position: Int): Library? {
    var i = position
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

  class ViewHolder(
      itemView: View,
      private val libraryListener: (library: Library) -> Unit
  ) : RecyclerView.ViewHolder(itemView) {
    var library: Library? = null
    private val title: TextView? = itemView.findViewById(R.id.title)
    private val license: TextView? = itemView.findViewById(R.id.license)

    init {
      if (itemViewType == R.layout.view_info_library) {
        itemView.setOnClickListener {
          library?.let { libraryListener(it) }
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
