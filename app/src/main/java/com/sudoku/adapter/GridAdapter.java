package com.sudoku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sudoku.R;
import com.sudoku.model.Sudoku;


import static com.sudoku.R.*;

// Adapter to generate sudoku board

public class GridAdapter extends BaseAdapter {
    private final Context mContext;
    public Sudoku grid;
    public String[] nonEngWords;
    public String[] engWords;
    String number_val;
    String number_val2;
    boolean listening = false; // listening mode
    private int[] boardSize;


    // 1
    public GridAdapter(Context context) {
        this.mContext = context;
    }

    // 2
    @Override
    public int getCount() {
        return grid.getCount();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int col = position % grid.getSideLength();
        int row = (position - col) / grid.getSideLength();
        boardSize = grid.getBoardSize();
        //get the view elements of the cell:
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(layout.cell, null);
        }
        final TextView cellValue = (TextView) convertView.findViewById(id.cell_value);
        final LinearLayout cellBackground = (LinearLayout) convertView.findViewById(id.cell_background);
        final LinearLayout GVcellBackground = (LinearLayout) convertView.findViewById(id.gridview_cell_background);

        //set the height of the cell:
        int height = parent.getHeight();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / grid.getSideLength());
        GVcellBackground.setLayoutParams(params);
        // colourize code boxes
        if (boardSize[0] == 9) {
            if ((row - (row  % 3)) == 0 && (col - (col % 3)) == 3) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row  % 3)) == 3 && (col - (col % 3)) == 0) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row  % 3)) == 3 && (col - (col % 3)) == 6) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row  % 3)) == 6 && (col - (col % 3)) == 3) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else {
                cellBackground.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        } else if (boardSize[0] == 4) {
            if ((row - (row % 2)) == 0 && (col - (col % 2)) == 2) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row % 2)) == 2 && (col - (col % 2)) == 0) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else {
                cellBackground.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        } else if (boardSize[0] == 6) {
            if ((row - (row % 2)) == 0 && (col - (col % 3)) == 3) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row % 2)) == 2 && (col - (col % 3)) == 0) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row % 2)) == 4 && (col - (col % 3)) == 3) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else {
                cellBackground.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        } else if (boardSize[0] == 12) {
            if ((row - (row % 3)) == 0 && (col - (col % 4)) == 4) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row % 3)) == 3 && (col - (col % 4)) == 0) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row % 3)) == 3 && (col - (col % 4)) == 8) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row % 3)) == 6 && (col - (col % 4)) == 4) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row % 3)) == 9 && (col - (col % 4)) == 0) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else if ((row - (row % 3)) == 9 && (col - (col % 4)) == 8) {
                cellBackground.setBackground(mContext.getResources().getDrawable(R.drawable.grid_button));
            } else {
                cellBackground.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        //change background:
        /*
        int value = grid.getValueAt(row, col);
        if (value == grid.getActiveValue()) {
            cellBackground.setBackgroundColor(Color.parseColor("#00ffff"));
        }
*/
        //bold if permanent
        // :
        /*
        boolean permanence = grid.getPermanenceAt(row, col);
        if (value != 0) {
            if (listening) {
                if (permanence) {
                    cellValue.setText(Integer.toString(value));
                    cellValue.setTypeface(null, Typeface.BOLD);
                } else {
                    cellValue.setText(engWords[value-1]);
                    cellValue.setTypeface(null, Typeface.NORMAL);
                }
            } else {
                if (permanence) {
                    cellValue.setText(engWords[value-1]);
                    cellValue.setTypeface(null, Typeface.BOLD);
                } else {
                    cellValue.setText(nonEngWords[value-1]);
                    cellValue.setTypeface(null, Typeface.NORMAL);
                }
            }
        } else {
            cellValue.setText("");
        }
*/
        cellValue.setText(grid.getWordAt(row, col));
        return convertView;
    }

    // setter method for listening mode
    public void isListening(boolean listening) {
        this.listening = listening;
    }

}

