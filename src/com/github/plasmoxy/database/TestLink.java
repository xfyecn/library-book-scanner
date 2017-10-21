package com.github.plasmoxy.database;
//muhaha

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.esy.playdotv.objects.Book;
import es.esy.playdotv.objects.Paper;

public class TestLink
{
	public static void main(String[] args)
	throws IOException, ClassNotFoundException
	{
		
		{ // save bok
			Paper currentPaper = new Book("34");
			currentPaper.setTitle("XD");
			currentPaper.setAuthor("Sebo");
			currentPaper.setBorrowDate(3, 5, 2005, 3, 5, 2006);
			
			Map<String, Paper> papers = SebuLink.load("papers.ser");
			papers.put("34", currentPaper);
			SebuLink.save("papers.ser", papers);
		}
		
		{ // load bok
			
			Map<String, Paper> papers = SebuLink.load("papers.ser");
			Paper currentPaper = papers.get("34");
			System.out.println(currentPaper.getTitle());
			System.out.println(currentPaper.getBorrowedUntilDate());
		}
	}
}
