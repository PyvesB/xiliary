package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContext.OVERLAY_OFFSET;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;

public class TreeLayoutContextTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 2, 4 );
    shell.open();
  }

  @Test
  public void initial() {
    TreeLayoutContext context = new TreeLayoutContext( tree );

    assertThat( context )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible()
      .hasPreferredSize( computePreferredTreeSize() )
      .hasVisibleArea( getVisibleArea() );
  }

  @Test
  public void preferredWidthExceedsVisibleAreaWidth() {
    shell.setSize( 200, 400 );
    expandTopBranch( tree );

    TreeLayoutContext context = new TreeLayoutContext( tree );

    assertThat( context )
      .verticalBarIsInvisible()
      .horizontalBarIsVisible();
  }

  @Test
  public void preferredHeightExceedsVisibleAreaHeight() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );

    TreeLayoutContext context = new TreeLayoutContext( tree );

    assertThat( context )
      .verticalBarIsVisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsInvisible();
  }

  @Test
  public void preferredSizeExceedsVisibleArea() {
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    TreeLayoutContext context = new TreeLayoutContext( tree );

    assertThat( context )
      .verticalBarIsVisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsVisible();
  }

  private Point computePreferredTreeSize() {
    return tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
  }

  private Rectangle getVisibleArea() {
    return shell.getClientArea();
  }

  private int expectedVerticalBarOffset() {
    int result = tree.getVerticalBar().getSize().x - BAR_BREADTH;
    if( result < 0 ) {
      result = OVERLAY_OFFSET - BAR_BREADTH;
    }
    return result;
  }

}