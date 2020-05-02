utf8 = require "utf8"

function point_in_area(x, y, area_x, area_y, area_w, area_h)
	return x >= area_x and y >= area_y and x <= area_x + area_w and y <= area_y + area_h
end

function utf8.sub(s, i, j)
    i = utf8.offset(s, i)
    j = utf8.offset(s, j + 1) - 1
    return string.sub(s, i, j)
end

function file_read(filename)
	local lines = {}
	for line in love.filesystem.lines(filename) do
		lines[#lines + 1] = line
	end
	return lines
end