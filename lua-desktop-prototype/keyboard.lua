require "class"
require "utils"
require "dictionary"

keyboard = class:new()

local KEYBOARD_WIDTH_Q = 0.8
local KEYBOARD_HEIGHT_Q = 0.45
local KEY_OFFSET_WIDTH_Q = 0.01
local KEY_OFFSET_HEIGHT_Q = 0.0056
local KEYS_PER_WIDTH = 5
local KEYS_PER_HEIGHT = 5
local SHUFFLE_TIME = 3
local SHUFFLE_MAX = 3
local ANIMATION_TIME = 0.15

function keyboard:init(cam, keyset)
	self.keyset = keyset
	self:setup_key_proportions(cam)
	self:init_keys()
end

function keyboard:init_keys()
	self.keys = {}

	for i=0, KEYS_PER_WIDTH-1 do
		self.keys[i] = {}
		
		for j=0, KEYS_PER_HEIGHT-1 do
			self.keys[i][j] = {
				letter = self:get_random_key(),
				animation_timer = 0,
				animation_type = nil
			}
		end
	end
end

function keyboard:get_random_key()
	local i = love.math.random(1, utf8.len(self.keyset))
	return utf8.sub(self.keyset, i, i)
end

function keyboard:setup_key_proportions(cam)
	self.keyboard_width = cam.virtual_w * KEYBOARD_WIDTH_Q
	self.keyboard_height = cam.virtual_h * KEYBOARD_HEIGHT_Q
	self.key_width = self.keyboard_width / KEYS_PER_WIDTH
	self.key_height = self.keyboard_height / KEYS_PER_HEIGHT
end

function keyboard:get_width()
	return self.keyboard_width
end

function keyboard:get_height()
	return self.keyboard_height
end

function keyboard:get_key(x, y)
	if x >= 0 and x < KEYS_PER_WIDTH and 
		y >= 0 and y < KEYS_PER_HEIGHT then
		return self.keys[x][y]
	end

	return nil
end

-- @returns:
-- 		false, false if out of area
--		true, false if in area but found bad letter
--		true, true if in area and found correct letter
function keyboard:click(relative_x, relative_y, expected_letter)
	local key = self:get_key(
		math.floor(relative_x / self.key_width),
		math.floor(relative_y / self.key_height))
	
	if key == nil then
		return false, false
	end

	if key.animation_type ~= nil then
		return true, false
	end

	if key.letter ~= expected_letter then
		key.animation_type = "go_red"
		return true, false
	end
	
	key.animation_type = "fade_out"
	return true, true
end

function keyboard:update_key_animation(key, dt)
	key.animation_timer = key.animation_timer + dt
	
	if key.animation_timer >= ANIMATION_TIME then
		key.animation_timer = 0
		
		if key.animation_type == "fade_out" then
			key.letter = self:get_random_key()
			key.animation_type = "fade_in"
		else
			key.animation_type = nil
		end
	end
end

function keyboard:foreach_key(fn)
	for i=0, KEYS_PER_WIDTH-1 do
		for j=0, KEYS_PER_HEIGHT-1 do
			fn(i, j)
		end
	end
end

function keyboard:update(dt)
	self:foreach_key(function(x, y)
		local key = self.keys[x][y]
		if key.animation_type ~= nil then
			self:update_key_animation(key, dt)
		end
	end)
end

function keyboard:key_animation_alpha(key)
	return math.min(1, key.animation_timer / ANIMATION_TIME)
end

function keyboard:get_key_color(key)
	local r, g, b = 115/255, 129/255, 241/255
	
	if key.animation_type == nil then
		return r, g, b, 1
	end
	
	local a = self:key_animation_alpha(key)
	
	if key.animation_type == "go_red" then
		return r, g * (1 - a), b * (1 - a), 1
	end
	
	return r, g, b, key.animation_type == "fade_out" and (1 - a) or a
end

function keyboard:get_text_color(key)
	local r, g, b = 1, 1, 1
	
	if key.animation_type == nil or key.animation_type == "go_red" then
		return r, g, b, 1
	end
	
	local a = self:key_animation_alpha(key)
	
	return r, g, b, key.animation_type == "fade_out" and (1 - a) or a
end

function keyboard:draw(cam, font)
	love.graphics.setFont(font)

	local font_h = font:getHeight()
	local offset_w = KEY_OFFSET_WIDTH_Q * self.key_width
	local offset_h = KEY_OFFSET_HEIGHT_Q * self.key_height
	local center_x = offset_w + self.key_width / 2
	local center_y = offset_h + self.key_height / 2
	
	self:foreach_key(function(x, y)
		local key = self.keys[x][y]
		local r, g, b, a = self:get_key_color(key)
		
		love.graphics.push()
		love.graphics.translate(x * self.key_width, y * self.key_height)

		love.graphics.setColor(r, g, b, a)
		love.graphics.rectangle(
			"fill", offset_w, offset_h,
			self.key_width - 2 * offset_w,
			self.key_height - 2 * offset_h)
		
		r, g, b, a = self:get_text_color(key)
		love.graphics.setColor(r, g, b, a)
		
		local text_w = font:getWidth(key.letter)
		love.graphics.print(key.letter, center_x - text_w / 2, center_y - font_h / 2)
		
		love.graphics.setColor(1, 1, 1, 1)
		love.graphics.pop()
	end)
end