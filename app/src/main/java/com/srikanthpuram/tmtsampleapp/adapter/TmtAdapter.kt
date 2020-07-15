package com.srikanthpuram.tmtsampleapp.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srikanthpuram.tmtsampleapp.R
import com.srikanthpuram.tmtsampleapp.model.Card

class TmtAdapter  : RecyclerView.Adapter<RecyclerView.ViewHolder> () {

    companion object {
        const val VIEW_TYPE_TITLE_DESCRIPTION = 1
        const val VIEW_TYPE_IMAGE_TITLE_DESCRIPTION = 2
    }

    //ViewHolder class to handle title and description
    inner class TitleCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)

        //bind the data to the textview's if the element has title or title and description only
        //handle only title/header of the list here
        fun bind(card: Card) = if(card.card_type.equals("text")) {   //set text and attributes for caption title here
            val textSize = card.card.attributes?.font?.size?.toFloat()
            if (textSize != null) {
                tvTitle.textSize = textSize
            }
            tvTitle.setTextColor(Color.parseColor(card.card.attributes?.text_color))
            tvTitle.setBackgroundColor(itemView.context.getColor(R.color.colorGrey))

            tvTitle.text = card.card.value
            tvDescription.visibility = View.GONE
        } else if(card.card_type.equals("title_description")) {   //set text and attributes for title and description here
            val textSize = card.card.title?.attributes?.font?.size?.toFloat()
            if(textSize != null) {
                tvTitle.textSize = textSize
            }
            tvTitle.setTextColor(Color.parseColor(card.card.title?.attributes?.text_color))
            tvTitle.text = card.card.title?.value

            val textDescriptionSize = card.card.description?.attributes?.font?.size?.toFloat()
            if (textDescriptionSize != null) {
                tvDescription.textSize = textDescriptionSize
            }
            tvDescription.setTextColor(Color.parseColor(card.card.description?.attributes?.text_color))
            tvDescription.text = card.card.description?.value
        } else {

        }
    }

    //ViewHolder class to handle title, description and image
    inner class ImageCardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.ivCardImage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)


        //bind the data to the textview's if the element has title and description and image
        fun bind(card: Card) {
            if (card.card_type.equals("image_title_description")) {
                val imageWidth: Int? = card.card.image?.size?.width?.toInt()
                val imageHeight: Int? = card.card.image?.size?.height?.toInt()

                if (imageWidth != null && imageHeight != null){
                    Glide.with(itemView.context).load(card.card.image.url)
                        .override(imageWidth,imageHeight)
                        .into(imageView)
                } else {
                    Glide.with(itemView.context).load(card.card.image?.url).into(imageView)
                }


                val textSize = card.card.title?.attributes?.font?.size?.toFloat()
                if (textSize != null) {
                    tvTitle.textSize = textSize
                }
                tvTitle.setTextColor(Color.parseColor(card.card.title?.attributes?.text_color))
                tvTitle.text = card.card.title?.value

                val textDescriptionSize = card.card.description?.attributes?.font?.size?.toFloat()
                if (textDescriptionSize != null) {
                    tvDescription.textSize = textDescriptionSize
                }
                tvDescription.setTextColor(Color.parseColor(card.card.description?.attributes?.text_color))
                tvDescription.text = card.card.description?.value
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Card> () {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.card == newItem.card
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        //create
        if (viewType == VIEW_TYPE_TITLE_DESCRIPTION) {
            return TitleCardViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_card_title_desc,
                    parent,
                    false
                )
            )
        } else {
            return ImageCardViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_card_title_image,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        val card = differ.currentList[position]

        //if the Json element contains only title/description, return VIEW_TYPE_TITLE_DESCRIPTION
        if( (card.card_type.equals("text")) || (card.card_type.equals("title_description"))){
            return  VIEW_TYPE_TITLE_DESCRIPTION
        }else {     //if the Json element contains only title, description, image  return VIEW_TYPE_IMAGE_TITLE_DESCRIPTION
            return VIEW_TYPE_IMAGE_TITLE_DESCRIPTION
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = differ.currentList[position]

        //if the Json element contains only title/description, return TitleCardViewHolder
        if(card.card_type.equals("text") || card.card_type.equals("title_description")) {
            (holder as TitleCardViewHolder).bind(card)
        } else {   //if the Json element contains only title, description, image return ImageCardViewHolder
            (holder as ImageCardViewHolder).bind(card)
        }
    }
}