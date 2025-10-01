package com.gogulf.passenger.app.ui.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.databinding.LayoutMenuItemsCardBinding

class MenuListAdapter (
    private val context: Context,
    private val menuList: ArrayList<MenuModels>
) : RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder>() {

    lateinit var onClick: OnMenuClicked

    fun onMenuClick(onMenuClicked: OnMenuClicked) {
        this.onClick = onMenuClicked
    }


    inner class MenuListViewHolder(itemView: LayoutMenuItemsCardBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        return MenuListViewHolder(
            LayoutMenuItemsCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val models = menuList[position]
//        Glide.with(context).load(models.icon).into(holder.binding.displayIcon)
        holder.binding.displayIcon.background = ContextCompat.getDrawable(context, models.icon)
        holder.binding.displayName.text = models.menuName
        holder.binding.container.setOnClickListener {
            onClick.onClicked(models)
        }
        if (menuList.size - 1 == position) {
            holder.binding.divider.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = menuList.size

    interface OnMenuClicked {
        fun onClicked(menuModels: MenuModels)
    }
}