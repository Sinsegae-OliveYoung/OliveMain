package com.olive.bound.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import com.olive.bound.model.BoundProductEditModel;
import com.olive.common.config.Config;
import com.olive.common.model.Bound;
import com.olive.common.model.BoundProduct;

public class ProductAddDialog extends JDialog {

    JTable table;
    JScrollPane scroll;
    JButton bt_add, bt_close;
    DefaultTableCellRenderer centerRenderer;
    BoundProductEditModel boundProductEditModel;
    BoundProduct selected;
    Bound bound;

    private boolean isConfirmed = false;
    
    private List<BoundProduct> selectedProducts = new ArrayList<>();

    public ProductAddDialog(JFrame parentFrame, BoundProduct selected) {
        super(parentFrame, "상품 추가", true);
        this.selected = selected;
        
        bound = selected.getBound();

        setSize(800, 500);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        // 테이블 모델
        boundProductEditModel = new BoundProductEditModel(bound.getBound_id());
        table = new JTable(boundProductEditModel);

        // 테이블 스타일
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(Config.LIGHT_GREEN);
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        
        table.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(new JTextField()) {
            {
                JTextField textField = (JTextField) getComponent();
                textField.setInputVerifier(new InputVerifier() {
                    @Override
                    public boolean verify(JComponent input) {
                        String text = ((JTextField) input).getText();
                        try {
                            Integer.parseInt(text);
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                });
            }
        });

        // 셀 가운데 정렬
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Config.WHITE);
        scroll.setPreferredSize(new Dimension(760, 380));

        // 버튼 패널
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bt_close = new JButton("닫기");
        bt_add = new JButton("추가");

        bottomPanel.add(bt_close);
        bottomPanel.add(bt_add);

        // 버튼 이벤트
        bt_close.addActionListener(e -> dispose());
        
        bt_add.addActionListener(e -> {
            selectedProducts = new ArrayList<>();
            BoundProductEditModel model = (BoundProductEditModel) table.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                BoundProduct bp = model.getBoundProductAt(i);
                if (bp.getB_count() > 0 && bp.getProductOption() != null && bp.getProductOption().getOption_id() != 0) {
                    selectedProducts.add(bp);
                }
            }
            isConfirmed = true; // ✅ 추가 버튼 눌린 경우만 true
            dispose();
        });

        
        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		if (SwingUtilities.isRightMouseButton(e)) {
        			int row = table.rowAtPoint(e.getPoint());
        			int column = table.columnAtPoint(e.getPoint());
        			
        			if (row >= 0 && column >= 0) {
        				System.out.println("클릭됨: row=" + row + ", column=" + column);
        				// 필요하면 해당 셀에 대한 팝업 메뉴 등도 여기에 구현 가능
        			}
        		}
        	}
        });

        // 컴포넌트 조립
        add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        
    }

    public List<BoundProduct> getSelectedProducts() {
        return selectedProducts;
    }


	public boolean isConfirmed() {
	    return isConfirmed;
	}
}
