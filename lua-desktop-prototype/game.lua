require "camera"
require "keyboard"
require "dictionary"

game = class:new()

function game:init(cam)
	self.keyboard = keyboard:new(cam, KEYSET["german"])
end

function game:mouse_released(x, y)
	self.keyboard:click(x, y, "A")
end

function game:update(dt)
	self.keyboard:update(dt)
end

function game:draw(cam, font)
	self.keyboard:draw(cam, font)
end