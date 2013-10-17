#pragma once

#include "Component.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example4
				{


					///
					/// <summary> * װ�����Ľӿڣ���Ҫ����װ�εĶ���ʵ��ͬ���Ľӿ� </summary>
					/// 
					class Decorator : public Component
					{
					///	
					///	 <summary> * ���б�װ�ε�������� </summary>
					///	 
					protected:
						Component *c;
					///	
					///	 <summary> * ͨ�����췽�����뱻װ�εĶ��� </summary>
					///	 * <param name="c��װ�εĶ���"> </param>
					///	 
					public:
						Decorator(Component *c);

						virtual double calcPrize(std::string user, Date *begin, Date *end);
					};

				}
			}
		}
	}
}