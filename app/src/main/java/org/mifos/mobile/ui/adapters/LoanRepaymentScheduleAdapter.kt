package org.mifos.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.Periods
import org.mifos.mobile.models.accounts.loan.tableview.Cell
import org.mifos.mobile.models.accounts.loan.tableview.ColumnHeader
import org.mifos.mobile.models.accounts.loan.tableview.RowHeader
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString
import javax.inject.Inject

/**
 * Created by Rajan Maurya on 04/03/17.
 */
class LoanRepaymentScheduleAdapter @Inject internal constructor() :
        AbstractTableAdapter<ColumnHeader?, RowHeader?, Cell?>() {

    private var currency: String? = ""
    private var columnWidth : Double = 0.0
    fun setCurrency(currency: String?) {
        this.currency = currency
    }

    internal inner class CellViewHolder(v: View) : AbstractViewHolder(v) {
        @JvmField
        @BindView(R.id.cell_data)
        var tvCell: TextView? = null

        @JvmField
        @BindView(R.id.cell_container)
        var llCellContainer : LinearLayout? = null

        init {
            ButterKnife.bind(this, v)
        }
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_loan_repayment_schedule, parent, false)
        return CellViewHolder(view)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val (data) = cellItemModel as Cell
        val period = data as Periods

        // Get the holder to update cell item text
        val viewHolder = holder as CellViewHolder
        var principal: Double?

        when (columnPosition) {
            0 -> viewHolder.tvCell?.text = getDateAsString(period.dueDate)
            1 -> {
                principal = period.principalOriginalDue
                if (principal == null) {
                    principal = 0.00
                }
                viewHolder.tvCell?.text = holder.itemView.context.getString(R.string.string_and_double,
                        currency, principal)
            }
            2 -> {
                principal = period.principalLoanBalanceOutstanding
                if (principal == null) {
                    principal = 0.00
                }
                viewHolder.tvCell?.text = holder.itemView.context.getString(R.string.string_and_string,
                        currency, formatCurrency(holder.itemView.context, principal))
            }
            else -> viewHolder.tvCell?.text = ""
        }
        viewHolder.llCellContainer?.layoutParams?.width = columnWidth.toInt()
        viewHolder.tvCell?.requestLayout()
    }

    internal inner class ColumnHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        @JvmField
        @BindView(R.id.column_header_textView)
        var tvColumnHeader: TextView? = null

        @JvmField
        @BindView(R.id.column_header_container)
        var llColumnHeaderContainer : LinearLayout? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.column_header_loan_repayment_schedule, parent, false)

        // Create a ColumnHeader ViewHolder
        return ColumnHeaderViewHolder(layout)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        position: Int
    ) {
        val (data) = columnHeaderItemModel as ColumnHeader

        // Get the holder to update cell item text
        val columnHeaderViewHolder = holder as ColumnHeaderViewHolder
        columnHeaderViewHolder.tvColumnHeader?.text = data.toString()

        columnHeaderViewHolder.llColumnHeaderContainer?.layoutParams?.width = columnWidth.toInt()
        columnHeaderViewHolder.tvColumnHeader?.requestLayout()
    }

    internal inner class RowHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        @JvmField
        @BindView(R.id.row_header_textview)
        var tvRowHeader: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.row_header_loan_repayment_schedule, parent, false)
        return RowHeaderViewHolder(layout)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        position: Int
    ) {
        val (data) = rowHeaderItemModel as RowHeader

        // Get the holder to update row header item text
        val rowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.tvRowHeader?.text = data.toString()
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.corner_view_loan_repayment_schedule, parent, false)
    }

    override fun getColumnHeaderItemViewType(columnPosition: Int): Int {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0
    }

    override fun getRowHeaderItemViewType(rowPosition: Int): Int {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0
    }

    override fun getCellItemViewType(columnPosition: Int): Int {
        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0
    }

    fun setColumnWidth(columnWidth : Double) {
        this.columnWidth = columnWidth
    }
}