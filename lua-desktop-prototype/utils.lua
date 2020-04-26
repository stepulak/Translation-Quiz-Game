function point_in_area(x, y, area_x, area_y, area_w, area_h)
	return x >= area_x and y >= area_y and x <= area_x + area_w and y <= area_y + area_h
end