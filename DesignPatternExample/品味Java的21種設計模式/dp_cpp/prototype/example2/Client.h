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
					/// <summary> * ʹ��ԭ�͵Ŀͻ��� </summary>
					/// 
					class Client
					{
					///	
					///	 <summary> * ������Ҫʹ�õ�ԭ�ͽӿڶ��� </summary>
					///	 
					private:
						Prototype *prototype;
					///	
					///	 <summary> * ���췽����������Ҫʹ�õ�ԭ�ͽӿڶ��� </summary>
					///	 * <param name="prototype"> ��Ҫʹ�õ�ԭ�ͽӿڶ��� </param>
					///	 
					public:
						Client(Prototype *prototype);
					///	
					///	 <summary> * ʾ�ⷽ����ִ��ĳ�����ܲ��� </summary>
					///	 
						virtual void operation();
					};
				}
			}
		}
	}
}