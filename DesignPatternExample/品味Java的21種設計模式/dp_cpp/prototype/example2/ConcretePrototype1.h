#pragma once

#include "Prototype.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace prototype
			{
				namespace example2
				{
					///
					/// <summary> * ��¡�ľ���ʵ�ֶ��� </summary>
					/// 
					class ConcretePrototype1 : public Prototype
					{
					public:
						virtual Prototype *clone();
					};


				}
			}
		}
	}
}