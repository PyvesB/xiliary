package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.VerticalScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class TableVerticalScrollBarUpdaterTest {

  private static final int ITEM_INDEX = 2;

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private TableVerticalScrollBarUpdater updater;
  private FlatScrollBar scrollbar;
  private Shell shell;
  private Table table;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    table = new TestTableFactory().create( shell );
    scrollbar = new FlatScrollBar( shell, SWT.VERTICAL );
    updater = new TableVerticalScrollBarUpdater( table, scrollbar );
    shell.open();
    flushPendingEvents();
  }

  @Test
  public void update() {
    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void updateWithSelection() {
    int expectedSelection = ITEM_INDEX * SELECTION_RASTER_SMOOTH_FACTOR;
    table.setTopIndex( 2 );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( expectedSelection );
  }

  @Test
  public void updateWithoutHeader() {
    table.setHeaderVisible( false );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void thumbRespectsHeader() {
    int before = updater.calculateThumb();
    table.setHeaderVisible( false );
    int after = updater.calculateThumb();

    assertThat( before ).isLessThan( after );
  }

  @Test
  public void ensureCorrectRounding() {
    int height = updater.calculateHeight();

    int actual = updater.calculateThumb();

    assertThat( actual ).isLessThan( SELECTION_RASTER_SMOOTH_FACTOR * height / table.getItemHeight() );
  }

  private int expectedMaximum() {
    return SELECTION_RASTER_SMOOTH_FACTOR * table.getItemCount();
  }
}