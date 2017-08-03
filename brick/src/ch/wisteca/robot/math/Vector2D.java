package ch.wisteca.robot.math;

/**
 * Représente un vecteur à 2 composantes.
 * @author Wisteca
 */
public class Vector2D
{
	public float x = 0, y = 0;
	
	public Vector2D()
	{
    	this(0, 0);
    }
	
  	/**
	 * Initialise un vecteur avec une composante donnée.
	 */
	public Vector2D(float x)
	{
		this(x, x);
	}
	
	/**
	 * Initialise un vecteur avec les composantes données.
	 */
	public Vector2D(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
    /**
	 * Initialise un vecteur par copie de v.
	 */
	public <T extends Vector2D> Vector2D(T v)
	{
		this(v.x, v.y);
	}
	
  	/**
     * return le produit scalaire sur lui-même.
     */
  	public float dot()
    {
    	return dot(this);
    }
  
    /**
     * return le produit scalaire sur v.
     */
  	public <T extends Vector2D> float dot(T v)
    {
    	return x * v.x + y * v.y;
    }
	
	/**
	 * @return la longueur du vecteur..
	 */
	public float length()
	{
		return (float) Math.sqrt(dot());
	}
  
  	/**
	 * @return la distance avec v.
	 */
	public <T extends Vector2D> float distance(T v)
	{
		return sub(v).length();
	}
	
	/**
	 * Reduit la longueur du vectur a 1.
	 * @return une copie modifiée du vecteur.
	 */
	public Vector2D normalize()
	{
		return div(length());
	}
	
	/**
	 * Additionne un autre vecteur.
	 * @return une copie modifiée du vecteur.
	 */
	public <T extends Vector2D> Vector2D add(T v)
	{
		return new Vector2D(x + v.x, y + v.y);
	}
  
	/**
	 * Additionne x et y aux composantes du vecteur.
	 * @return une copie modifiée du vecteur
	 */
	public Vector2D add(float x, float y)
	{
		return add(new Vector2D(x, y));
	}
  
	/**
	 * Additionne x aux composantes du vecteur.
	 * @return une copie modifiée du vecteur
	 */
	public Vector2D add(float x)
	{
		return add(x, x);
	}
	
	/**
	 * Soustrait un autre vecteur.
	 * @return une copie modifiée du vecteur.
	 */
	public <T extends Vector2D> Vector2D sub(T v)
	{
		return new Vector2D(x - v.x, y - v.y);
	}
  
	/**
	 * Soustrait x et y aux composantes du vecteur.
	 * @return une copie modifiée du vecteur
	 */
	public Vector2D sub(float x, float y)
	{
		return sub(new Vector2D(x, y));
	}
  
	/**
	 * Soustrait x aux composantes du vecteur.
	 * @return une copie modifiée du vecteur
	 */
	public Vector2D sub(float x)
	{
		return sub(x, x);
	}
	
	/**
	 * Multiplie un autre vecteur.
	 * @return une copie modifiée du vecteur.
	 */
	public <T extends Vector2D> Vector2D mul(T v)
	{
		return new Vector2D(x * v.x, y * v.y);
	}
  
	/**
	 * Multiplie x et y aux composantes du vecteur.
	 * @return une copie modifiée du vecteur
	 */
	public Vector2D mul(float x, float y)
	{
		return mul(new Vector2D(x, y));
	}
  
	/**
	 * Multiplie x aux composantes du vecteur.
	 * @return une copie modifiée du vecteur
	 */
	public Vector2D mul(float x)
	{
		return mul(x, x);
	}
	
	/**
	 * Divise un autre vecteur.
	 * @return une copie modifiée du vecteur.
	 */
	public <T extends Vector2D> Vector2D div(T v)
	{
		return new Vector2D(x / v.x, y / v.y);
	}
  
	/**
	 * Divise x et y aux composantes du vecteur.
	 * @return une copie modifiée du vecteur
	 */
	public Vector2D div(float x, float y)
	{
		return div(new Vector2D(x, y));
	}
  
	/**
	 * Divise x aux composantes du vecteur.
	 * @return une copie modifiée du vecteur
	 */
	public Vector2D div(float x)
	{
		return div(x, x);
	}
  
	/**
	 * Inverse le vecteur.
	 * @return une copie modifiée du vecteur
	 */
  	public Vector2D rev()
    {
    	return new Vector2D(this).div(-1f);
    }
    
  	/**
  	 * @return un vecteur initialisé avec les composantes x
  	 */
  	public Vector2D xx()
  	{
  		return new Vector2D(x, x);
  	}
  	
  	/**
  	 * @return un vecteur initialisé avec les composantes y
  	 */
  	public Vector2D yy()
  	{
  		return new Vector2D(y, y);
  	}
  	
  	/**
  	 * @return un vecteur initialisé avec les composantes y x
  	 */
  	public Vector2D yx()
  	{
  		return new Vector2D(y, x);
  	}
}
