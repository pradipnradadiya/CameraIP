package com.packetalk.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.list_group_assign_camera.view.*
import kotlinx.android.synthetic.main.list_item_assign_camera.view.*

class ExpandableGroupListAdapter(
    val _context: Context,
    _listDataHeader: ArrayList<Groups>
    // child data in format of header title, child title
) : BaseExpandableListAdapter() {


    private var _listDataHeaderFiltered: ArrayList<Groups> = _listDataHeader
    private var _listDataHeaderOriginal = ArrayList<Groups>()

    init {
        _listDataHeaderOriginal.addAll(_listDataHeader)
    }

    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        AppLogger.e(groupPosition.toString())
        AppLogger.e(childPosititon.toString())
        return _listDataHeaderFiltered[groupPosition].cameraDetailsFull[childPosititon].cameraName
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView

        val childText = getChild(groupPosition, childPosition) as String

        if (convertView == null) {
            val infalInflater = this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_item_assign_camera, null)
        }

        convertView!!.expandedListItem.text = childText

        /*val txtListChild = convertView!!
                .findViewById(R.id.lblListItem) as TextView
        txtListChild.text = childText
        */
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return _listDataHeaderFiltered[groupPosition].cameraDetailsFull.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return this._listDataHeaderFiltered[groupPosition].groupName
    }

    override fun getGroupCount(): Int {
        return this._listDataHeaderFiltered.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            val infalInflater = this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_group_assign_camera, null)
        }

        if (groupPosition % 2 == 1) {
//            convertView?.setBackgroundResource(R.color.red)
        } else {
//            convertView?.setBackgroundResource(R.color.red)
        }

        convertView!!.listTitle.text = headerTitle

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}