#pragma once

#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example6
				{

					///
					/// <summary> * ������������ </summary>
					/// 
					class Component
					{
					///	
					///	 <summary> * ��¼��������� </summary>
					///	 
					private:
						Component *parent;

					///	
					///	 <summary> * ��ȡһ������ĸ�������� </summary>
					///	 * <returns> һ������ĸ�������� </returns>
					///	 
					public:
						virtual Component *getParent();
					///	
					///	 <summary> * ����һ������ĸ�������� </summary>
					///	 * <param name="parent"> һ������ĸ�������� </param>
					///	 
						virtual void setParent(Component *parent);
					///	
					///	 <summary> * ����ĳ���������������� </summary>
					///	 * <returns> ĳ���������������� </returns>
					///	 
						virtual std::vector<Component*> getChildren();
					/*-------------------������ԭ�еĶ���----------------------*/	

					///	
					///	 <summary> * ��������������� </summary>
					///	 
						virtual void printStruct(std::string preStr) = 0;

					///	
					///	 <summary> * ����϶����м����������  </summary>
					///	 * <param name="child"> ��������϶����е�������� </param>
					///	 
						virtual void addChild(Component *child);

					///	
					///	 <summary> * ����϶������Ƴ�ĳ��������� </summary>
					///	 * <param name="child"> ���Ƴ���������� </param>
					///	 
						virtual void removeChild(Component *child);

					///	
					///	 <summary> * ����ĳ��������Ӧ��������� </summary>
					///	 * <param name="index"> ��Ҫ��ȡ����������������������0��ʼ </param>
					///	 * <returns> ������Ӧ��������� </returns>
					///	 
						virtual Component *getChildren(int index);
					};


				}
			}
		}
	}
}