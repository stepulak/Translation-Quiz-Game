require "camera"
require "keyboard"
require "dictionary"
require "input_array"

local KEYBOARD_OFFSET_BOTTOM_Q = 0.02
local INPUT_ARRAY_OFFSET_TOP_Q = 0.3

game = class:new()

local d = load_dictionary("dictionaries/czech_german.txt", "Cesko - Nemecký", "czech", "german")

function game:init(cam)
	self.keyboard = keyboard:new(cam, KEYSET["german"])
	self.input_array = input_array:new(cam, d.words[1][2])
end

function game:keyboard_position(cam)
	local keyboard_w = self.keyboard:get_width()
	local keyboard_h = self.keyboard:get_height()
	local offset_bottom = cam.virtual_h * KEYBOARD_OFFSET_BOTTOM_Q
	
	return (cam.virtual_w - keyboard_w) / 2,
		cam.virtual_h - keyboard_h - offset_bottom
end

function game:input_array_position(cam)
	local input_array_w = self.input_array:get_max_width()
	local input_array_h = self.input_array:get_max_height()
	
	return (cam.virtual_w - input_array_w) / 2,
		cam.virtual_h * INPUT_ARRAY_OFFSET_TOP_Q
end

function game:mouse_released(cam, x, y)
	local kx, ky = self:keyboard_position(cam)
	kx = x - kx
	ky = y - ky
	self.keyboard:click(kx, ky, "A")
end

function game:update(dt)
	self.keyboard:update(dt)
	self.input_array:update(dt)
end

function game:draw(cam, font)
	love.graphics.setBackgroundColor(221/255, 219/255, 222/255, 1)
	
	-- Draw keyboard
	local keyboard_x, keyboard_y = self:keyboard_position(cam)
	love.graphics.push()
	love.graphics.translate(keyboard_x, keyboard_y)
	self.keyboard:draw(cam, font)
	love.graphics.pop()
	
	-- Draw input array
	local input_array_x, input_array_y = self:input_array_position(cam)
	love.graphics.push()
	love.graphics.translate(input_array_x, input_array_y)
	self.input_array:draw(cam, font)
	love.graphics.pop()
end