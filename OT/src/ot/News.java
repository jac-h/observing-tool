// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
//import gemini.gui.PresentationWindow;
//import gemini.gui.WindowManager;

package ot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URL;

import jsky.app.ot.gui.RichTextBoxWidgetExt;

/**
 * This class has been moved to the package ot and modified for use of swing instead of
 * freebongo widget.
 *
 * @author M.Folger (based on the class News in orac2/OT/ot/src, freebongo OT)
 */
public final class News extends JFrame
{
	private static News _news;
	private RichTextBoxWidgetExt _rt = new RichTextBoxWidgetExt();
	private JButton _close = new JButton( "Close" );

	public synchronized static void showNews( URL url )
	{
		if( _news == null )
		{
			_news = new News();
			_news._initTextBox();

			BufferedReader br = null;
			try
			{
				br = new BufferedReader( new InputStreamReader( url.openStream() ) );
				_news._readNews( br );
			}
			catch( IOException ex )
			{
				_news._warning( "Couldn't read the news file!" );
				System.out.println( "IO EXCEPTION: " + ex );
			}
			finally
			{
				try
				{
					if( br != null )
						br.close();
				}
				catch( Exception ex ){}
			}
		}
		_news.show();
		_news.toFront();
		_news.setState( JFrame.NORMAL );
	}

	public synchronized static void hideNews()
	{
		if( _news != null )
			_news.hide();
	}

	private News()
	{
		super( "Observing Tool Release Notes" );

		_rt.setEditable( false );

		_close.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				hideNews();
			}
		} );

		getContentPane().add( BorderLayout.CENTER , new JScrollPane( _rt ) );

		JPanel bottomPanel = new JPanel();
		bottomPanel.add( _close );
		getContentPane().add( BorderLayout.SOUTH , bottomPanel );

		setBounds( 100 , 100 , 480 , 640 );
	}

	private void _initTextBox()
	{
		_rt.setText( "" );

		_rt.append( "JAC Observing Tool Release Notes" );
		_rt.append( "\n\n" );
		_rt.append( "This page will be updated frequently as new features are " + "incorporated and bugs are fixed." );
		_rt.append( "\n\n" );
	}

	public void show()
	{
		super.show();
	}

	private void _warning( String warning )
	{
		_rt.append( "WARNING: " );
	}

	private void _readNews( BufferedReader br ) throws IOException
	{
		String line;
		while( ( line = br.readLine() ) != null )
		{
			if( line.startsWith( "+++" ) )
			{
				_rt.append( "\n" );
				_rt.append( line.substring( 4 ) );
			}
			else
			{
				line = line.trim();
				if( line.equals( "" ) )
					_rt.append( "\n\n" );
				else
					_rt.append( line + " " );
			}
		}
	}
}