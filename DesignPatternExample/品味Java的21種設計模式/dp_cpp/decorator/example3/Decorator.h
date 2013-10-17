#pragma once

#include "Component.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example3
				{

					///
					/// <summary> * װ�����ӿڣ�ά��һ��ָ���������Ľӿڶ���
					/// * ������һ��������ӿ�һ�µĽӿ� </summary>
					/// 
					class Decorator : public Component
					{
					///	
					///	 <summary> * ����������� </summary>
					///	 
					protected:
						Component *component;

					///	
					///	 <summary> * ���췽��������������� </summary>
					///	 * <param name="component"> ������� </param>
					///	 
					public:
						Decorator(Component *component);

						virtual void operation();


					};


				}
			}
		}
	}
}