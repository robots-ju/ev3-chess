package ch.wisteca.robot.math;

public class Vec2
{
	public float x = 0, y = 0;
	
	public Vec2()
	{
    	this(0, 0);
    }
  
	public Vec2(float x)
	{
		this(x, x);
	}
	
	public Vec2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
  	//-------------------------------
  
  	public float dot()
    {
    	return dot(this);
    }
  
  	public float dot(Vec2 v)
    {
    	return x * v.x + y * v.y;
    }
	
	public float length()
	{
		return (float) Math.sqrt(dot());
	}
  
	public float distance(Vec2 v)
	{
		return sub(v).length();
	}
	
	public Vec2 normalize()
	{
		return div(length());
	}
	
  	public Vec2 rev()
    {
    	return div(-1f);
    }
  	
  	public Vec2 reflect(Vec2 normale)
  	{
  		return normale.mul(-2f * dot(normale)).normalize();
  	}
  	
  	public Vec2 clamp(float min, float max)
  	{
  		Vec2 v = new Vec2();
  		
  		v.x = clamp(x, min, max);
  		v.y = clamp(y, min, max);
  		return v;
  	}
  	
  	public Vec2 mix(Vec2 v, float percent)
  	{
  		return v.sub(this).mul(clamp(percent, 0, 1)).add(this);
  	}
  	
  	//-------------------------------
	
	public Vec2 add(Vec2 v)
	{
		return add(v.x, v.y);
	}
	
	public Vec2 add(float x)
	{
		return add(x, x);
	}
  
	public Vec2 add(float x, float y)
	{
		return new Vec2(this.x + x, this.y + y);
	}
	
	public Vec2 sub(Vec2 v)
	{
		return sub(v.x, v.y);
	}
	
	public Vec2 sub(float x)
	{
		return sub(x, x);
	}
  
	public Vec2 sub(float x, float y)
	{
		return new Vec2(this.x - x, this.y - y);
	}
	
	public Vec2 mul(Vec2 v)
	{
		return mul(v.x, v.y);
	}
	
	public Vec2 mul(float x)
	{
		return mul(x, x);
	}
  
	public Vec2 mul(float x, float y)
	{
		return new Vec2(this.x * x, this.y * y);
	}
	
	public Vec2 div(Vec2 v)
	{
		return div(v.x, v.y);
	}
	
	public Vec2 div(float x)
	{
		return div(x, x);
	}
  
	public Vec2 div(float x, float y)
	{
		return new Vec2(this.x / x, this.y / y);
	}

  	//-------------------------------
  	
  	public Vec2 xx()
  	{
  		return new Vec2(x, x);
  	}
  	
  	public Vec2 yy()
  	{
  		return new Vec2(y, y);
  	}
  	
  	public Vec2 xy()
  	{
  		return new Vec2(x, y);
  	}
  	
  	public Vec2 yx()
  	{
  		return new Vec2(y, x);
  	}
  	
  	@Override
  	public String toString()
  	{
  		StringBuilder sb = new StringBuilder();
  		
  		sb.append("Vec2@")
  		.append(hashCode())
  		.append(" {x: ")
  		.append(x)
  		.append(", y: ")
  		.append(y)
  		.append('}');
  		return sb.toString();
  	}
  	
	public static float clamp(float value, float min, float max)
	{
		return Math.min(Math.max(value, min), max);
	}
}