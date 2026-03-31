Studying again

This code creates a sphere of points used to generate a world:
´´´csharp
using UnityEngine;

public class VoxelTest : MonoBehaviour
{
    public int size = 16;
    public float spacing = 1f;

    float[,,] density;

    void Start()
    {
        GenerateDensity();
        VisualizeSurfacePoints();
    }

    float Density(Vector3 p)
    {
        float radius = size * 0.4f;
        Vector3 center = Vector3.one * size * 0.5f;
        return radius - Vector3.Distance(p, center);
    }

    void GenerateDensity()
    {
        density = new float[size + 1, size + 1, size + 1];

        for (int x = 0; x <= size; x++)
            for (int y = 0; y <= size; y++)
                for (int z = 0; z <= size; z++)
                    density[x, y, z] = Density(new Vector3(x, y, z));
    }

    void VisualizeSurfacePoints()
    {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                for (int z = 0; z < size; z++)
                {
                    Vector3 p = new Vector3(x, y, z);
                    float d = density[x, y, z];
                    float dX = density[x + 1, y, z];

                    if ((d > 0 && dX < 0) || (d < 0 && dX > 0))
                    {
                        Vector3 pX = p + Vector3.right;
                        Vector3 surfacePoint = Interpolate(p, pX, d, dX);
                        SpawnMarker(surfacePoint);
                    }
                }
    }

    Vector3 Interpolate(Vector3 p, Vector3 pX, float d, float dX)
    {
        float t = d / (d - dX);
        return Vector3.Lerp(p, pX, t);
    }

    void SpawnMarker(Vector3 pos)
    {
        GameObject sphere = GameObject.CreatePrimitive(PrimitiveType.Sphere);
        sphere.transform.localScale = Vector3.one * 0.2f;
        sphere.transform.position = pos * spacing;
        sphere.transform.SetParent(transform);
    }
}
´´´

First of all this:
´´´csharp
float Density(Vector3 p)
{
    float radius = size * 0.4f;
    Vector3 center = Vector3.one * size * 0.5f;
    return radius - Vector3.Distance(p, center);
}
´´´

every point (p) returns a number:
- if the number is greater than 0 the point is outside the surface.
- If its 0 its exactly on the surface
- If its smaller than 0 its outside the sphere.
We are ofcorse placing all out point on the surface and ignoring the rest.
This code is the core of the algorithm because it will determine all the relevant points used to generate the world.

This is the logic that creates the circle. When generating a 3D world that isn't just a heightmap you require density and creating a circle is perfect for this purpose.

´´´
float radius = size * 0.4f;
´´´
First we define the radius of the grid. 
size is used to define a grid, we always work with grids in procedural generation, radius is the diameter /2 ofcorse but by making the radius smaller than .5 we can make sure there won't be any weird outer edges on the outside of the chunk.

´´´
Vector3 center = Vector3.one * size * 0.5f;
´´´
Then we get the center point which is ofcorse the grid size (Vector3.one * size) * .5f which equals half.

´´´
return radius - Vector3.Distance(p, center);
´´´
Then we check where the point is, Vector3.Distance will return the distance between p and the center. 
Then we do this minus radius. So if radius - distance = 0 we know its an outer edge as explained earlier.


Ofcorse we still need to actually feed the points into the Density function and that's what this function does:
´´´csharp
void GenerateDensity()
{
	density = new float[size + 1, size + 1, size + 1];

	for (int x = 0; x <= size; x++)
		for (int y = 0; y <= size; y++)
			for (int z = 0; z <= size; z++)
			{
				density[x, y, z] = Density(new Vector3(x, y, z));
			}
}
´´´

´´´csharp
density = new float[size + 1, size + 1, size + 1];
´´´
First a 3D array is generated, its size is hardcoded to have up to 16 spots in each direction. We add +1 because an array counts from 0 and we need all grid corners.

´´´csharp
for (int x = 0; x <= size; x++)
for (int y = 0; y <= size; y++)
for (int z = 0; z <= size; z++)
´´´
We then loop over every direction, in total 4096 will happen for each potential point 16*16*16.

´´´
density[x, y, z] = Density(new Vector3(x, y, z));
´´´
And then we will sent each point to the Density function and see if its location is on the edge of our form that we made which means its a valid point.


We now declared our surfacepoints which is technically all there is to it, now we visualize the parts around our skeleton, currently its just balls that visualize these points. 
´´´csharp
void VisualizeSurfacePoints()
{
	for (int x = 0; x < size; x++)
		for (int y = 0; y < size; y++)
			for (int z = 0; z < size; z++)
			{
				Vector3 p = new Vector3(x, y, z);

				float d = density[x, y, z];

				float dX = density[x + 1, y, z];

				if ((d > 0 && dX < 0) || (d < 0 && dX > 0))
				{
					Vector3 p2 = p + Vector3.right;

					Vector3 surfacePoint = Interpolate(p, p2, d, dX);

					SpawnMarker(surfacePoint);
				}
			}
}
´´´

Once again this will be looped through all the grid points again:
´´´
for (int x = 0; x < size; x++)
for (int y = 0; y < size; y++)
for (int z = 0; z < size; z++)
´´´

We then grab the density value assigned for each 3D coordinate that we determined earlier
´´´csharp
float d = density[x, y, z];
´´´

After that we also grab the density of the neighbour from the x axis. 
We need this because one point doesn't tell us anything, why is that? Because these density values don't magically have a 0 position, maybe a rare chance of it happening but usually it just doesn't. So we don't check individual points rather that if one desisty is below 0 and the one next to it is over 0 we know for sure the 0 is inbetween then.
´´´csharp
float dX = density[x + 1, y, z];
´´´

This is exactly what happens here, we only execute the next code if it has one value over 0 and one below
´´´
if ((d > 0 && dX < 0) || (d < 0 && dX > 0))
´´´


Now we will get the other coordinate, earlier we got the 2 densities to find which combination of densities will make us find the 0. p is our current grid point p2 is one grid point to the right.
´´´csharp
Vector3 p2 = p + Vector3.right;
´´´

Now we have to interpolate these values to find the actual 0 point:
´´´csharp
Vector3 surfacePoint = Interpolate(p, pX, d, dX);

Vector3 Interpolate(Vector3 p, Vector3 pX, float d, float dX)
{
	float t = d / (d - dX);
	return Vector3.Lerp(p, pX, t);
}
´´´

we start with this line:
´´´csharp
float t = d / (d - dX);
´´´
first of all we aim to find where the density becomes 0. This is done by first finding the difference between our actual density and the one on its right. 
It is best shown with numbers: 3 / (3 - (-1)) = 3 / 4 = 0.75. As you can see this calculation shows 75% meaning if these where our values the halfway point would be 75% from point A to point B.

Then we use lerp to find the exact 0 location between the 2 coordinated and use t to find the distances in between 
´´´
return Vector3.Lerp(p, pX, t);
´´´

we then use that coordinate to spawn a marker but that underlying code isn't very relevant


