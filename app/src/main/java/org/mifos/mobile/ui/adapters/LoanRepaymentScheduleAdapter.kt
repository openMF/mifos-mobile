package org.mifos.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.accounts.loan.Periods
import org.mifos.mobile.core.model.entity.accounts.loan.tableview.Cell
import org.mifos.mobile.core.model.entity.accounts.loan.tableview.ColumnHeader
import org.mifos.mobile.core.model.entity.accounts.loan.tableview.RowHeader
import org.mifos.mobile.databinding.CellLoanRepaymentScheduleBinding
import org.mifos.mobile.databinding.ColumnHeaderLoanRepaymentScheduleBinding
import org.mifos.mobile.databinding.CornerViewLoanRepaymentScheduleBinding
import org.mifos.mobile.databinding.RowHeaderLoanRepaymentScheduleBinding
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString
import javax.inject.Inject

/**
 * Created by Rajan Maurya on 04/03/17.
 */
class LoanRepaymentScheduleAdapter @Inject internal constructor() :
    AbstractTableAdapter<ColumnHeader?, RowHeader?, Cell?>() {

    private var currency: String? = ""
    private var columnWidth: Double = 0.0
    fun setCurrency(currency: String?) {
        this.currency = currency
    }

    internal inner class CellViewHolder(private val bindingCellView: CellLoanRepaymentScheduleBinding) :
        AbstractViewHolder(bindingCellView.root) {
        fun bind(columnPosition: Int, period: Periods) {
            with(bindingCellView) {
                var principal: Double?
                when (columnPosition) {
                    0 -> cellData.text = getDateAsString(period.dueDate)
                    1 -> {
                        principal = period.principalOriginalDue
                        if (principal == null) {
                            principal = 0.00
                        }
                        cellData.text = itemView.context.getString(
                            R.string.string_and_double,
                            currency,
                            principal,
                        )
                    }

                    2 -> {
                        principal = period.principalLoanBalanceOutstanding
                        if (principal == null) {
                            principal = 0.00
                        }
                        cellData.text = itemView.context.getString(
                            R.string.string_and_string,
                            currency,
                            formatCurrency(itemView.context, principal),
                        )
                    }

                    else -> cellData.text = ""
                }
                cellContainer.layoutParams?.width = columnWidth.toInt()
                cellData.requestLayout()
            }
        }
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val bindingCellView = CellLoanRepaymentScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return CellViewHolder(bindingCellView)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int,
    ) {
        val (data) = cellItemModel as Cell
        val period = data as Periods

        holder as CellViewHolder
        holder.bind(columnPosition, period)
    }

    internal inner class ColumnHeaderViewHolder(private val bindingColumnHeader: ColumnHeaderLoanRepaymentScheduleBinding) :
        AbstractViewHolder(bindingColumnHeader.root) {
        fun bind(columnHeaderText: String) {
            with(bindingColumnHeader) {
                columnHeaderTextView.text = columnHeaderText
                columnHeaderContainer.layoutParams?.width = columnWidth.toInt()
                columnHeaderTextView.requestLayout()
            }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AbstractViewHolder {
        val bindingColumnHeader = ColumnHeaderLoanRepaymentScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ColumnHeaderViewHolder(bindingColumnHeader)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        position: Int,
    ) {
        val (data) = columnHeaderItemModel as ColumnHeader

        holder as ColumnHeaderViewHolder
        holder.bind(data.toString())
    }

    internal inner class RowHeaderViewHolder(private val bindingRowHeader: RowHeaderLoanRepaymentScheduleBinding) :
        AbstractViewHolder(bindingRowHeader.root) {
        fun bind(rowHeaderText: String) {
            bindingRowHeader.rowHeaderTextview.text = rowHeaderText
        }
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val bindingRowHeader = RowHeaderLoanRepaymentScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return RowHeaderViewHolder(bindingRowHeader)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        position: Int,
    ) {
        val (data) = rowHeaderItemModel as RowHeader
        holder as RowHeaderViewHolder
        holder.bind(data.toString())
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        val bindingRowHeader = CornerViewLoanRepaymentScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return bindingRowHeader.root
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

    fun setColumnWidth(columnWidth: Double) {
        this.columnWidth = columnWidth
    }
}
