#include "Leaf.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example7
				{

					Leaf::Leaf(std::string name)
					{
						InitializeInstanceFields();
						this->name = name;
					}

					std::string Leaf::getName()
					{
						return this->name;
					}

					void Leaf::printStruct(std::string preStr)
					{
						puts(preStr+"-"+name);
					}
				}
			}
		}
	}
}
