#pragma once

#include "Aggregate.h"
#include "ConcreteAggregate.h"
#include "Iterator.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example1
				{

					class Client
					{
					///	
					///	 <summary> * ʾ�ⷽ����ʹ�õ������Ĺ��ܡ�
					///	 * ����ʾ��ʹ�õ������������ۺ϶��� </summary>
					///	 
					public:
						virtual void someOperation();
						static void main(std::string args[]);
					};

				}
			}
		}
	}
}