#pragma once

#include "Iterator.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example3
				{

					///
					/// <summary> * �ۺ϶���Ľӿڣ����崴����Ӧ����������Ľӿ� </summary>
					/// 
					class Aggregate
					{
					///	
					///	 <summary> * ����������������Ӧ����������Ľӿ� </summary>
					///	 * <returns> ��Ӧ����������Ľӿ� </returns>
					///	 
					public:
						virtual Iterator *createIterator() = 0;
					};
				}
			}
		}
	}
}