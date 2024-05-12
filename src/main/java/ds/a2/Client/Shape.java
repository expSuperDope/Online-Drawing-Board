package ds.a2.Client;
import ds.a2.DrawType;
import ds.a2.EraserSize;

import java.awt.*;
import java.util.List;

public class Shape {

    private DrawType type;
    private Point startPoint, endPoint;
    private Color color;
    private List<Point> path;
    private String text;
    private EraserSize size;

    //For basic shape(Line, Circle, Ovel, Rectangle)
    public Shape(DrawType type, Point s, Point e, Color color)
    {
        this.type = type;
        this.startPoint = s;
        this.endPoint = e;
        this.color = color;
        this.path = null;
        this.text = null;
        this.size = EraserSize.Small;
    }

    //For free draw
    public Shape(DrawType type, List<Point> path, Color color)
    {
        this.type = type;
        this.startPoint = null;
        this.endPoint = null;
        this.color = color;
        this.path = path;
        this.text = null;
        this.size = EraserSize.Small;
    }

    //For eraser
    public Shape(DrawType type, List<Point> path, EraserSize size)
    {
        this.type = type;
        this.startPoint = null;
        this.endPoint = null;
        this.color = Color.WHITE;
        this.path = path;
        this.text = null;
        this.size = size;
    }

    //For eraser line shape
    public Shape(DrawType type, Point s, Point e, EraserSize size)
    {
        this.type = type;
        this.startPoint = s;
        this.endPoint = e;
        this.color = Color.WHITE;
        this.path = null;
        this.text = null;
        this.size = size;
    }

    //For text
    public Shape(DrawType type, Point s, String text, Color color)
    {
        this.type = type;
        this.startPoint = s;
        this.endPoint = null;
        this.color = color;
        this.path = null;
        this.text = text;
        this.size = EraserSize.Small;
    }

    public DrawType get_Type()
    {
        return type;
    }

    public void generate_shape(Graphics2D g2d) {
        g2d.setColor(color);
        if (type == DrawType.Line) 
        {
            Stroke stroke = new BasicStroke(1);
            if(size == EraserSize.Medium)
            {
                stroke = new BasicStroke(20);
            }
            else if (size == EraserSize.Large)
            {
                stroke = new BasicStroke(50);
            }
            
            g2d.setStroke(stroke);
            g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        } 
        else if (type == DrawType.Circle) 
        {
            int s_p_x = Math.min(startPoint.x, endPoint.x);
            int s_p_y = Math.min(startPoint.y, endPoint.y);
            int width = Math.max(Math.abs(startPoint.x - endPoint.x), Math.abs(startPoint.y - startPoint.y));
            int height = width;
            g2d.drawOval(s_p_x, s_p_y, width, height);
        } 
        else if (type == DrawType.Oval) 
        {
            int s_p_x = Math.min(startPoint.x, endPoint.x);
            int s_p_y = Math.min(startPoint.y, endPoint.y);
            int width = Math.abs(startPoint.x - endPoint.x);
            int height = Math.abs(startPoint.y - endPoint.y);
            g2d.drawOval(s_p_x, s_p_y, width, height);
        } 
        else if (type == DrawType.Rectangle) 
        {
            int s_p_x = Math.min(startPoint.x, endPoint.x);
            int s_p_y = Math.min(startPoint.y, endPoint.y);
            int width = Math.abs(startPoint.x - endPoint.x);
            int height = Math.abs(startPoint.y - endPoint.y);
            g2d.drawRect(s_p_x, s_p_y, width, height);
        } 
        else if (type == DrawType.Free || type == DrawType.Eraser) 
        {
            Stroke stroke = new BasicStroke(1);
            if(size == EraserSize.Medium)
            {
                stroke = new BasicStroke(20);
            }
            else if (size == EraserSize.Large)
            {
                stroke = new BasicStroke(50);
            }
            
            g2d.setStroke(stroke);
            for (int i = 0; i < path.size() - 1; i++) 
            {
                g2d.drawLine(path.get(i).x, path.get(i).y, path.get(i+1).x, path.get(i+1).y);
            }
        }
        else if(type == DrawType.Text)
        {
            g2d.drawString(text, startPoint.x, startPoint.y);
        }
    }
}