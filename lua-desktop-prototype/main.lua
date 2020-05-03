require "camera"
require "game"

local font = love.graphics.newFont(80)
local cam = camera:new(1080, 1920, 1080 / 3, 1920 / 3)
local game = game:new(cam, font)

function love.load()
	love.window.setMode(cam.real_w, cam.real_h)
end

function love.mousereleased(x, y)
	local inv_scale_x, inv_scale_y = cam:get_virtual_scale_inv()
	game:mouse_released(cam, x * inv_scale_x, y * inv_scale_y)
end

function love.update(dt)
	game:update(dt)
end

function love.draw()
	local scale_x, scale_y = cam:get_virtual_scale()
	
	love.graphics.push()
	love.graphics.scale(scale_x, scale_y)
	
	game:draw(cam, font)
	
	love.graphics.pop()
end